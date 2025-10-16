package com.xtechcn.cloud.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.payment.constants.AttributeKeyNames;
import com.xtechcn.cloud.payment.constants.TradeStateEnum;
import com.xtechcn.cloud.payment.entity.Trade;
import com.xtechcn.cloud.payment.mapper.TradeMapper;
import com.xtechcn.cloud.payment.service.TradeService;
import com.xtechcn.commom.payment.constants.XPayConstants;
import com.xtechcn.commom.payment.model.*;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.core.utils.IParamUtil;
import com.xtechcn.common.serialization.lang.JsonObj;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;


/**
 * 交易信息表
 *
 * @author Alay
 * @since 2022-05-16 10:17:37
 */
@Service
@RequiredArgsConstructor
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade> implements TradeService {

    @Override
    public XPayTradeModel generate(XPayTradeModel tradeModel) {
        boolean isOk;
        try {
            Trade trade = this.createTrade(tradeModel);
            isOk = this.save(trade);
        } catch (Exception ex) {
            // 交易单存入库中失败
            this.failureNotice(tradeModel, ex.getMessage());
            throw new RollBackException("trade generate failed", ex);
        }
        // 交易单生成不成功,直接抛出异常
        if (!isOk) {
            this.failureNotice(tradeModel, "trade generate failed");
            throw new RollBackException("trade generate failed");
        }
        return tradeModel;
    }

    @Override
    public boolean isPaid(String tradeNo) {
        Trade trade = this.getById(tradeNo);
        if (null == trade) return false;
        return TradeStateEnum.PAID == trade.getState();
    }

    @Override
    public void refundSuccessful(XPayRefundModel refundModel) {
        Trade trade = this.getById(refundModel.tradeNo());
        // 避免重复处理
        if (TradeStateEnum.REFUND == trade.getState()) return;

        // 已经退款的金额
        MoneyPenny refundAmount = trade.getRefundAmount().add(refundModel.refundAmount());
        trade.setRefundAmount(refundAmount);
        // 如果退款金额已经全部退完了，修改状态
        if (refundAmount.eq(trade.getPayAmount())) {
            trade.setState(TradeStateEnum.REFUND);
        }
        this.updateById(trade);
    }

    @Override
    public void refundUnsuccessful(XPayRefundModel refundModel, XPayResult<?> xPayResult) {
        // 失败退款,回滚退款(常规情况下不需要手动回滚,避免重复退款)
        this.refundRollback(refundModel);
    }

    @Override
    public void refundRollback(XPayRefundModel refundModel) {
        Trade trade = this.getById(refundModel.tradeNo());
        // 已经退款的金额
        MoneyPenny failureAmount = trade.getRefundAmount().subtract(refundModel.refundAmount());
        // 单回退到退款金额已经小于 0 时,不做处理了
        if (MoneyPenny.ZERO.gt(failureAmount)) return;

        trade.setRefundAmount(failureAmount);
        // 逆向回滚状态
        if (TradeStateEnum.REFUND == trade.getState()) {
            trade.setState(TradeStateEnum.PAID);
        }
        this.updateById(trade);
    }

    @Override
    public XPayTradeModel findOne(String tradeNo) {
        Trade trade = this.getById(tradeNo);
        DefaultTradeModel tradeModel = DefaultTradeModel.builder()
                .tradeNo(tradeNo)
                .batchNo(trade.getBatchNo())
                .channelKey(trade.getChannelKey())
                .payer(trade.getPayerId())
                .payAmount(trade.getPayAmount())
                .totalAmount(trade.getAmount())
                .tradeType(trade.getTradeType())
                .subject(trade.getSubject())
                .body(trade.getBody())
                .build();

        // 可退款金额
        Optional.ofNullable(trade.getRefundAmount()).ifPresent(refundAmount -> tradeModel.addAttribute(AttributeKeyNames.REFUND_AMOUNT, refundAmount));
        // 扩展额外数据(订单数据)
        JsonObj extra = IParamUtil.ofValue(trade.getExtra(), new JsonObj());
        // 订单数据
        Optional.ofNullable(extra.getValue(XPayConstants.ORDERS, ArrayList.class)).ifPresent(orders -> {
            List<DefaultTradeOrder> orderList = new ArrayList<>();
            for (Object order : orders) {
                LinkedHashMap<String, Object> map = (LinkedHashMap) order;
                DefaultTradeOrder tradeOrder = DefaultTradeOrder.builder()
                        .orderNo(IMapUtil.getStr(map, "orderNo"))
                        .payee(IMapUtil.getStr(map, "payee"))
                        .amount(MoneyPenny.valueOfYuan(IMapUtil.getDecimal(map, "amount")))
                        .payAmount(MoneyPenny.valueOfYuan(IMapUtil.getDecimal(map, "payAmount")))
                        .build();
                orderList.add(tradeOrder);
            }
            tradeModel.setOrders(orderList);
        });
        // appId
        Optional.ofNullable(extra.getStr(AttributeKeyNames.APP_ID)).ifPresent(app -> tradeModel.addAttribute(AttributeKeyNames.APP_ID, app));
        // 渠道商户ID
        Optional.ofNullable(extra.getStr(AttributeKeyNames.WX_MCHID)).ifPresent(mchId -> tradeModel.addAttribute(AttributeKeyNames.WX_MCHID, mchId));
        // 币种
        Optional.ofNullable(trade.getCurrency()).ifPresent(cur -> tradeModel.addAttribute(XPayConstants.CURRENCY_KEY, cur));
        // ip地址信息
        Optional.ofNullable(trade.getClientIp()).ifPresent(ip -> tradeModel.addAttribute(XPayConstants.CLIENT_IP_KEY, ip));
        // 时间
        Optional.ofNullable(trade.getCreateTime()).ifPresent(time -> tradeModel.addAttribute(AttributeKeyNames.CREATE_TIME, time));
        Optional.ofNullable(trade.getUpdateTime()).ifPresent(time -> tradeModel.addAttribute(AttributeKeyNames.UPDATE_TIME, time));
        Optional.ofNullable(trade.getSuccessTime()).ifPresent(time -> tradeModel.addAttribute(AttributeKeyNames.SUCCESS_TIME, time));
        // 用户
        Optional.ofNullable(trade.getCreateBy()).ifPresent(createBy -> tradeModel.addAttribute(AttributeKeyNames.CREATE_BY, createBy));
        // 设备信息
        Optional.ofNullable(trade.getDevice()).ifPresent(device -> tradeModel.addAttribute(XPayConstants.DEVICE_KEY, device));
        // 消息内容
        Optional.ofNullable(trade.getMessage()).ifPresent(message -> tradeModel.addAttribute(XPayConstants.MESSAGE_KEY, message));
        // 付款状态
        tradeModel.addAttribute(AttributeKeyNames.STATE, trade.getState());
        // 回调地址
        Optional.ofNullable(trade.getNotifyUrl()).ifPresent(url -> tradeModel.setNotifyUrl(IJsonUtil.parseObject(url, XPayNotify.class)));
        return tradeModel;
    }

    private Trade createTrade(XPayTradeModel tradeModel) {
        // 生成交易单
        Trade trade = new Trade();
        trade.setTradeNo(tradeModel.tradeNo());
        trade.setBatchNo(tradeModel.batchNo());
        trade.setChannelKey(tradeModel.channelKey());
        // 支付渠道格式: channel_ofApp
        trade.setChannel(tradeModel.channelKey().substring(tradeModel.channelKey().lastIndexOf("_") + 1));
        trade.setPayerId(tradeModel.payer());

        // 金额
        trade.setAmount(tradeModel.totalAmount());
        trade.setPayAmount(tradeModel.payAmount());

        // 订单类型
        trade.setTradeType(tradeModel.tradeType());

        // 币种
        Optional.ofNullable(tradeModel.attribute(XPayConstants.CURRENCY_KEY)).ifPresent(cur -> trade.setCurrency(cur.toString()));

        trade.setState(TradeStateEnum.NOT_PAY);
        // trade.setClientIp(tradeModel.attribute(XPayConstants.CLIENT_IP_KEY).toString());
        trade.setDevice(tradeModel.attribute(XPayConstants.DEVICE_KEY).toString());
        trade.setSubject(tradeModel.subject());
        // 扩展数据
        JsonObj extra = new JsonObj();
        // appId
        Optional.ofNullable(tradeModel.attribute(AttributeKeyNames.APP_ID)).ifPresent(app -> extra.put(AttributeKeyNames.APP_ID, app.toString()));
        // 微信支付的商户Id
        Optional.ofNullable(tradeModel.attribute(AttributeKeyNames.WX_MCHID)).ifPresent(mchId -> extra.put(AttributeKeyNames.WX_MCHID, mchId.toString()));
        // 附加信息（存放订单数据）
        Optional.ofNullable(tradeModel.orders()).ifPresent(orders -> extra.put(XPayConstants.ORDERS, orders));
        trade.setBody(tradeModel.body());
        trade.setExtra(extra);
        // 回调地址
        trade.setNotifyUrl(IJsonUtil.toJson(tradeModel.notifyUrl()));
        return trade;
    }

    private void failureNotice(XPayTradeModel tradeModel, String message) {
        // 进行支付失败通知
    }

}
