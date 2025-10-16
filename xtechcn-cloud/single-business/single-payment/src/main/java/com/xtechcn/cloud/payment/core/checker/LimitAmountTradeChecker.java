package com.xtechcn.cloud.payment.core.checker;

import com.xtechcn.cloud.api.payment.RemotePayNotifyService;
import com.xtechcn.commom.payment.checker.XPayTradeModelChecker;
import com.xtechcn.commom.payment.constants.XPayConstants;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.XPayNotify;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.common.autoconfigure.utils.SpringBeanUtil;
import com.xtechcn.common.core.lang.MoneyPenny;

import java.util.HashMap;
import java.util.Map;

/**
 * 单笔交易最大限额检查
 *
 * @author Alay
 * @since 2024-09-03 11:17
 */
public class LimitAmountTradeChecker implements XPayTradeModelChecker {

    @Override
    public XPayTradeModel check(XPayTradeModel tradeModel) {
        MoneyPenny limitMaxAmount = tradeModel.attribute(XPayConstants.LIMIT_MAX_AMOUNT);
        if (null == limitMaxAmount) return tradeModel;

        // 单次交易最大限额
        if (limitMaxAmount.gt(tradeModel.payAmount())) {
            String message = String.format("单次交易金额必须小于 %s 元", limitMaxAmount.toYuan().toString());
            // 支付失败,通知订单
            XPayNotify xPayNotify = tradeModel.notifyUrl();
            if (null != xPayNotify) {
                RemotePayNotifyService notifyService = findNotifyService(xPayNotify.getFailureUrl());
                if (null != notifyService) {
                    // 通知订单支付失败的处理
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("tradeNo", tradeModel.tradeNo());
                    parameters.put("message", message);
                    notifyService.failedPayNotify(parameters);
                }
            }
            // 单次交易金额小于最大允许金额
            throw new XPayException(message);
        }
        return tradeModel;
    }

    private static RemotePayNotifyService findNotifyService(String failureUrl) {
        Map<String, RemotePayNotifyService> payNotifyServiceMap = SpringBeanUtil.getBeansOfType(RemotePayNotifyService.class);
        return payNotifyServiceMap.get(failureUrl);
    }

}
