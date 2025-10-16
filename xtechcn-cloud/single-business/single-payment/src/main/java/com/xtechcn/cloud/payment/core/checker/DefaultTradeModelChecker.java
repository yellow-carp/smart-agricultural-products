package com.xtechcn.cloud.payment.core.checker;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.xtechcn.cloud.api.payment.RemotePayNotifyService;
import com.xtechcn.commom.payment.checker.XPayTradeModelChecker;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.DefaultTradeModel;
import com.xtechcn.commom.payment.model.DefaultTradeOrder;
import com.xtechcn.commom.payment.model.XPayNotify;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.common.autoconfigure.utils.SpringBeanUtil;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.utils.IMapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基于验证的参数处理器
 *
 * @author Alay
 * @since 2023-06-11 05:00
 */
public class DefaultTradeModelChecker implements XPayTradeModelChecker {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public XPayTradeModel check(XPayTradeModel tradeModel) {
        XPayNotify xPayNotify = tradeModel.notifyUrl();
        if (null == xPayNotify || StrUtil.isBlank(xPayNotify.verifyUrl())) {
            logger.warn("***> trading verify configuration is blank <***");
            // 数据验证
            simpleVerifyTrade(tradeModel);
            return tradeModel;
        }

        RemotePayNotifyService verifyService = findVerifyService(xPayNotify.getVerifyUrl());
        if (null == verifyService) throw new XPayException("***> verify service not be null <***");

        // // 请求参数
        Map<String, Object> parameters = IMapUtil.toMap(tradeModel);
        // 远端调用,验证支付并返回数据
        Map<String, Object> verifyResult = verifyService.preTradeVerify(parameters);
        // Map 数据转换为 交易单对象
        tradeModel = transform(tradeModel, verifyResult);
        Collection<XPayTradeModel.TradeOrder> orders = tradeModel.orders();
        if (null == orders || orders.isEmpty()) throw new XPayException("***> trade order infos not be empty <***");

        // 数据校验
        withOrderVerifyTrade(tradeModel);
        return tradeModel;
    }


    private static XPayTradeModel transform(XPayTradeModel beforeData, Map<String, Object> verifyResult) {
        if (null == verifyResult) {
            throw new XPayException("***> Transaction order verification must be performed before trading <***");
        }
        // 查询订单模块相应的
        DefaultTradeModel before = (DefaultTradeModel) beforeData;

        List<Map<String, Object>> orders = IMapUtil.getValue(verifyResult, "orders", ArrayList.class);
        List<DefaultTradeOrder> tradeOrders = new ArrayList<>();
        for (Map<String, Object> orderInfo : orders) {
            DefaultTradeOrder defaultTradeOrder = DefaultTradeOrder.builder()
                    .orderNo(IMapUtil.getStr(orderInfo, "orderNo"))
                    .payee(IMapUtil.getStr(orderInfo, "payee"))
                    .amount(IMapUtil.getValue(orderInfo, "amount", MoneyPenny.class))
                    .payAmount(IMapUtil.getValue(orderInfo, "payAmount", MoneyPenny.class))
                    .build();
            tradeOrders.add(defaultTradeOrder);
        }
        before.setOrders(tradeOrders);
        before.setPayAmount(IMapUtil.getValue(verifyResult, "payAmount", MoneyPenny.class));
        before.setTotalAmount(IMapUtil.getValue(verifyResult, "totalAmount", MoneyPenny.class));
        before.setTradeType(IMapUtil.getInt(verifyResult, "tradeType"));
        before.setSubject(IMapUtil.getStr(verifyResult, "subject"));
        String body = IMapUtil.getStr(verifyResult, "body");
        // body 不能太长,微信的最长127
        Assert.checkBetween(body.length(), 1, 127, () -> new XPayException("***> body length out of range <***"));
        before.setBody(body);

        // 通知地址信息
        XPayNotify notifyUrl = before.getNotifyUrl();
        // 需要将检验支付时,处理的支付成功和失败的同志地址填写
        notifyUrl.setSuccessUrl(IMapUtil.getStr(verifyResult, "successUrl"));
        notifyUrl.setFailureUrl(IMapUtil.getStr(verifyResult, "failureUrl"));
        return before;
    }

    private static void withOrderVerifyTrade(XPayTradeModel tradeModel) {
        Collection<XPayTradeModel.TradeOrder> orders = tradeModel.orders();
        if (null == orders || orders.isEmpty()) throw new XPayException("***> order not empty <***");

        // 金额计算
        MoneyPenny totalPayAmount = MoneyPenny.ZERO;
        MoneyPenny totalAmount = MoneyPenny.ZERO;

        for (XPayTradeModel.TradeOrder order : orders) {
            // 金额必须大于 0
            if (NumberUtil.isLess(order.payAmount().value(), MoneyPenny.ZERO.value())) {
                throw new XPayException("***> pay amount must be greater than or equal zero <***");
            }
            // 付款总金额
            totalPayAmount = totalPayAmount.add(order.payAmount().value());
            if (NumberUtil.isLess(order.amount().value(), MoneyPenny.ZERO.value())) {
                throw new XPayException("***> amount must be greater than or equal zero <***");
            }
            // 交易总金额
            totalAmount = totalAmount.add(order.amount().value());
            // 收款方账号不能空
            Assert.notBlank(order.payee(), () -> new XPayException("***> payee not be blank <***"));
        }
        // 实际付款金额处理
        ((DefaultTradeModel) tradeModel).setTotalAmount(totalAmount);
        ((DefaultTradeModel) tradeModel).setPayAmount(totalPayAmount);

        // 其他参数验证
        simpleVerifyTrade(tradeModel);
    }

    private static void simpleVerifyTrade(XPayTradeModel tradeModel) {
        // 订单类型不能空
        Assert.notNull(tradeModel.tradeType(), () -> new XPayException("***> order type not be null <***"));
        // 付款信息数据不能空
        Assert.notBlank(tradeModel.subject(), () -> new XPayException("***> subject not be blank <***"));
        Assert.notBlank(tradeModel.body(), () -> new XPayException("***> body not be blank <***"));

        // 总金额验证
        Assert.notNull(tradeModel.totalAmount(), () -> new XPayException("***> total amount not be null <***"));
        Assert.notNull(tradeModel.payAmount(), () -> new XPayException("***> pay amount not be null <***"));
    }


    private static RemotePayNotifyService findVerifyService(String verifyUrl) {
        Map<String, RemotePayNotifyService> payRpcServiceMap = SpringBeanUtil.getBeansOfType(RemotePayNotifyService.class);
        return payRpcServiceMap.get(verifyUrl);
    }

}
