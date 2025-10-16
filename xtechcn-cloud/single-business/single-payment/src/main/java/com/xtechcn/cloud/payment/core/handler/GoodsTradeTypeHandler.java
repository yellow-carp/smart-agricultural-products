package com.xtechcn.cloud.payment.core.handler;

import com.xtechcn.cloud.api.payment.RemotePayNotifyService;
import com.xtechcn.commom.payment.handler.TradeTypeHandler;
import com.xtechcn.commom.payment.model.XPayNotify;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.common.autoconfigure.utils.SpringBeanUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品交易类型处理器
 *
 * @author Alay
 * @since 2025-09-18 17:12
 */
public class GoodsTradeTypeHandler implements TradeTypeHandler {
    @Override
    public boolean supports(int tradeType) {
        return 1 == tradeType;
    }

    @Override
    public void handler(XPayTradeModel tradeModel, XPayResult<?> payResult) {
        // 订单类型拦截器处理
        Collection<XPayTradeModel.TradeOrder> orders = tradeModel.orders();

        XPayNotify xPayNotify = tradeModel.notifyUrl();

        RemotePayNotifyService successNotifyService = findSuccessNotifyService(xPayNotify.successUrl());
        if (null == successNotifyService) return;

        for (XPayTradeModel.TradeOrder order : orders) {
        /*
        PaidOrderData paidOrder = PaidOrderData.empty()
                    .tradeNo(tradeModel.tradeNo())
                    .batchNo(tradeModel.batchNo())
                    .orderNo(order.orderNo())
                    .orderType(order.orderType())
                    .amount(order.amount())
                    .payAmount(order.payAmount())
                    .channelKey(tradeModel.channelKey())
                    .payer(tradeModel.payer())
                    .payee(order.payee())
                    // 交易单类型
                    .tradeType(tradeModel.tradeType())
                    // 订单类型
                    .orderType(order.orderType())
                    // 支付成功后通知调用方
                    .successUrl(tradeModel.notifyUrl().successUrl());
        */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("tradeNo", tradeModel.tradeNo());
            parameters.put("tradeType", tradeModel.tradeType());
            parameters.put("orderNo", order.orderNo());
            successNotifyService.successPayNotify(parameters);
        }
    }

    private static RemotePayNotifyService findSuccessNotifyService(String successUrl) {
        Map<String, RemotePayNotifyService> beansOfTypeMap = SpringBeanUtil.getBeansOfType(RemotePayNotifyService.class);
        return beansOfTypeMap.get(successUrl);
    }

}