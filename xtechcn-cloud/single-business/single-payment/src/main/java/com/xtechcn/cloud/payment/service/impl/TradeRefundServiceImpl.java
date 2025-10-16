package com.xtechcn.cloud.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.api.payment.RemotePayNotifyService;
import com.xtechcn.cloud.api.payment.model.RemoteRefundModel;
import com.xtechcn.cloud.payment.constants.AttributeKeyNames;
import com.xtechcn.cloud.payment.constants.PaymentResult;
import com.xtechcn.cloud.payment.constants.RefundStateEnum;
import com.xtechcn.cloud.payment.constants.TradeStateEnum;
import com.xtechcn.cloud.payment.entity.TradeRefund;
import com.xtechcn.cloud.payment.mapper.TradeRefundMapper;
import com.xtechcn.cloud.payment.service.TradeRefundService;
import com.xtechcn.cloud.payment.service.TradeService;
import com.xtechcn.commom.payment.api.XPayIntegrationService;
import com.xtechcn.commom.payment.channel.XPayChannel;
import com.xtechcn.commom.payment.channel.XPayChannelNotify;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.*;
import com.xtechcn.common.autoconfigure.utils.SpringBeanUtil;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.utils.IStrUtil;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 交易信息表
 *
 * @author Alay
 * @since 2022-05-16 10:17:37
 */
@Service
@RequiredArgsConstructor
public class TradeRefundServiceImpl extends ServiceImpl<TradeRefundMapper, TradeRefund> implements TradeRefundService {

    private final TradeService tradeService;
    private final XPayChannelRepository xPayChannelRepository;
    private final XPayIntegrationService xPayIntegrationService;


    @Override
    public List<TradeRefund> listByTrade(String tradeNo) {
        return this.list(Wrappers.<TradeRefund>lambdaQuery().eq(TradeRefund::getTradeNo, tradeNo));
    }

    @Override
    public boolean isPaid(String refundNo) {
        TradeRefund refund = this.getById(refundNo);
        return null != refund && RefundStateEnum.REFUNDED == refund.getState();
    }

    @Override
    public boolean isRefund(String refundNo) {
        TradeRefund tradeRefund = this.getById(refundNo);
        if (null == tradeRefund) return true;
        return RefundStateEnum.REFUNDED == tradeRefund.getState();
    }

    @Override
    public void successful(XPayRefundModel refundModel, XPayResult<?> xPayResult) {
        TradeRefund tradeRefund = this.getById(refundModel.refundNo());
        // 判断状态，避免重复处理
        if (RefundStateEnum.REFUNDED == tradeRefund.getState()) return;

        tradeRefund = TradeRefund.withRefundNo(refundModel.refundNo());

        // 主动查询
        if (XPayResult.WAY_QUERY == xPayResult.way()) {
            tradeRefund.setRequest("主动查询");
        }
        tradeRefund.setState(RefundStateEnum.REFUNDED);
        // 当前时间作为时间
        tradeRefund.setNotifyTime(System.currentTimeMillis() + "");
        tradeRefund.setMessage("退款成功");
        // 响应数据
        tradeRefund.setResponse(IJsonUtil.toJson(xPayResult.data()));
        this.updateById(tradeRefund);

        // 成功后通知退款方
        this.successRefundNotify(refundModel);
    }

    @Override
    public void unsuccessful(XPayRefundModel refundModel, XPayResult<?> xPayResult) {
        TradeRefund tradeRefund = this.getById(refundModel.refundNo());
        // 判断状态，避免重复处理
        if (RefundStateEnum.FAILED == tradeRefund.getState()) return;

        tradeRefund = TradeRefund.withRefundNo(refundModel.refundNo());
        // 主动查询
        if (XPayResult.WAY_QUERY == xPayResult.way()) {
            tradeRefund.setRequest("主动查询");
        }
        tradeRefund.setState(RefundStateEnum.FAILED);
        // 当前时间作为时间
        tradeRefund.setNotifyTime(System.currentTimeMillis() + "");
        tradeRefund.setMessage(xPayResult.message());
        // 响应数据
        tradeRefund.setResponse(IJsonUtil.toJson(xPayResult.data()));
        this.updateById(tradeRefund);

        // 成功后通知退款方
        this.failedRefundNotify(refundModel, xPayResult);
    }

    @Override
    public boolean refundRollback(XPayRefundModel refundModel) {
        TradeRefund tradeRefund = TradeRefund.withRefundNo(refundModel.refundNo());
        tradeRefund.setState(RefundStateEnum.ROLLBACK);
        // 当前时间作为时间
        tradeRefund.setNotifyTime(System.currentTimeMillis() + "");
        tradeRefund.setMessage("退款失败回滚");
        return this.updateById(tradeRefund);
    }

    @Override
    public XPayRefundModel generate(XPayRefundModel refundModel) {
        TradeRefund tradeRefund = new TradeRefund();
        // 退款单号
        tradeRefund.setRefundNo(refundModel.refundNo());
        // 外部退款标识
        tradeRefund.setOutRefundNo(refundModel.outRefundNo());
        // 原交易单号
        tradeRefund.setTradeNo(refundModel.tradeNo());
        // 流水系列号(微信支付订单号,中金支付则为批次号)
        tradeRefund.setSerialNo(refundModel.serialNo());
        // 退款说明
        tradeRefund.setReason(refundModel.reason());
        // 回调地址（存的是一个Json对象）
        tradeRefund.setNotifyUrl(IJsonUtil.toJson(refundModel.notifyUrl()));
        // 币种
        tradeRefund.setCurrency(refundModel.currency());
        // 退款方编码
        tradeRefund.setPayerId(refundModel.payer());
        // 退款金额
        tradeRefund.setRefundAmount(refundModel.refundAmount());
        // 退款金额(分)
        tradeRefund.setTotalAmount(refundModel.amount());
        // 额外参数
        tradeRefund.setExtra(IJsonUtil.toJson(refundModel.attributes()));
        // 退款资金来源
        tradeRefund.setFromBy(refundModel.tradeNo());
        // 支付状态
        tradeRefund.setState(RefundStateEnum.ACCEPTING);
        // 支付渠道唯一标识
        tradeRefund.setChannelKey(refundModel.channelKey());
        // 订单支付成功时间
        Optional.ofNullable(refundModel.attribute("successTime")).ifPresent(time -> tradeRefund.setSuccessTime((Long) time));
        boolean isOk = this.save(tradeRefund);

        if (!isOk) throw new RollBackException("退款单生成失败了");
        return refundModel;
    }

    @Override
    public XPayRefundModel findOne(String refundNo) {
        TradeRefund tradeRefund = this.getById(refundNo);
        if (null == tradeRefund) throw new XPayException("退款单不存在");

        DefaultRefundModel defaultRefundModel = DefaultRefundModel.builder()
                .refundNo(tradeRefund.getRefundNo())
                .outRefundNo(tradeRefund.getOutRefundNo())
                .tradeNo(tradeRefund.getTradeNo())
                .serialNo(tradeRefund.getSerialNo())
                .reason(tradeRefund.getReason())
                .notifyUrl(IJsonUtil.parseObject(tradeRefund.getNotifyUrl(), XPayNotify.class))
                .currency(tradeRefund.getCurrency())
                .payer(tradeRefund.getPayerId())
                .amount(tradeRefund.getTotalAmount())
                .refundAmount(tradeRefund.getRefundAmount())
                .channelKey(tradeRefund.getChannelKey()).build();

        // 响应信息
        Optional.ofNullable(tradeRefund.getResponse()).ifPresent(response ->
                defaultRefundModel.addAttribute("response", IJsonUtil.<HashMap<String, Object>>parseObject(response, HashMap.class)));
        // 其他额外信息
        String extra = tradeRefund.getExtra();
        if (IStrUtil.isNotBlank(extra) && IJsonUtil.isJson(extra)) {
            Map<String, Object> extraParameters = IJsonUtil.<HashMap<String, Object>>parseObject(extra, HashMap.class);
            defaultRefundModel.addAttributes(extraParameters);
        }

        if (StrUtil.isNotBlank(tradeRefund.getMessage())) {
            defaultRefundModel.addAttribute("message", tradeRefund.getMessage());
        }
        defaultRefundModel.addAttribute("state", tradeRefund.getState()).addAttribute("successTime", tradeRefund.getSuccessTime());

        return defaultRefundModel;
    }

    @Override
    public void refund(RemoteRefundModel refundModel) {
        // 付款的交易单
        XPayTradeModel tradeModel = tradeService.findOne(refundModel.getTradeNo());
        // 状态判断
        if (TradeStateEnum.PAID != tradeModel.attribute(AttributeKeyNames.STATE)) {
            throw new XPayException(PaymentResult.TRADE_STATE_INVALID.getMessage());
        }
        // 是否还有退款的金额验证
        MoneyPenny refundAmount = tradeModel.attribute(AttributeKeyNames.REFUND_AMOUNT);
        // 允许退款的金额 = 支付金额 - 已付款金额
        MoneyPenny refund = tradeModel.payAmount().subtract(refundAmount);
        if (Objects.requireNonNull(refundModel.getAmount()).gt(refund)) {
            // 退款金额大于可退款金额
            throw new XPayException(PaymentResult.REFUND_AMOUNT_GT.getMessage());
        }
        // 支付渠道配置信息
        XPayChannel xPayChannel = xPayChannelRepository.findOne(tradeModel.channelKey());
        if (null == xPayChannel) {
            xPayChannel = xPayChannelRepository.findOne(xPayChannelRepository.parseChannel(tradeModel.channelKey()));
        }
        // 构建支付退款单
        XPayChannelNotify xPayChannelNotify = xPayChannel.channelNotify();
        DefaultRefundModel defaultRefundModel1 = DefaultRefundModel.builder()
                .notifyUrl(XPayNotify.builder().callbackUrl(xPayChannelNotify.refundNotify()).build())
                .build();
        // 记录退款单记录
        this.generate(defaultRefundModel1);

        // 执行退款
        XPayResult<?> xPayResult = xPayIntegrationService.refundTrade(defaultRefundModel1);
        this.refundProcess(defaultRefundModel1, xPayResult);
    }


    public void refundProcess(XPayRefundModel refundModel, XPayResult<?> xPayResult) {
        // 退款失败,发生异常了
        if (xPayResult.hasError()) {
            this.unsuccessful(refundModel, xPayResult);
        }
        if (xPayResult.success()) {
            // 锁定交易单退款金额
            this.successful(refundModel, xPayResult);
        }
        // 交易单更新（先更新为已成功退款流程，后会查询退款结果，若退款失败，会返回金额数字）
        tradeService.refundSuccessful(refundModel);
    }

    private void successRefundNotify(XPayRefundModel refundModel) {
        XPayNotify xPayNotify = refundModel.notifyUrl();
        RemotePayNotifyService refundNotifyService = findRefundNotifyService(xPayNotify.getSuccessUrl());
        if (null == refundNotifyService) return;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tradeNo", refundModel.tradeNo());
        parameters.put("outRefundNo", refundModel.outRefundNo());
        refundNotifyService.successRefundNotify(parameters);
    }

    private void failedRefundNotify(XPayRefundModel refundModel, XPayResult<?> xPayResult) {
        XPayNotify xPayNotify = refundModel.notifyUrl();
        RemotePayNotifyService refundNotifyService = findRefundNotifyService(xPayNotify.getFailureUrl());
        if (null == refundNotifyService) return;

        String message = xPayResult.message();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tradeNo", refundModel.tradeNo());
        parameters.put("outRefundNo", refundModel.outRefundNo());
        parameters.put("message", message);
        refundNotifyService.failedRefundNotify(parameters);
    }


    private static RemotePayNotifyService findRefundNotifyService(String clazzName) {
        Map<String, RemotePayNotifyService> payNotifyServiceMap = SpringBeanUtil.getBeansOfType(RemotePayNotifyService.class);
        return payNotifyServiceMap.get(clazzName);
    }

}
