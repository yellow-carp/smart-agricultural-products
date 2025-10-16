package com.xtechcn.cloud.payment.core.handler;

import com.xtechcn.cloud.payment.constants.TradeStateEnum;
import com.xtechcn.cloud.payment.entity.Trade;
import com.xtechcn.cloud.payment.entity.TradeNotify;
import com.xtechcn.cloud.payment.service.TradeNotifyService;
import com.xtechcn.cloud.payment.service.TradeService;
import com.xtechcn.commom.payment.constants.XPayChannelEnum;
import com.xtechcn.commom.payment.handler.TradeTypeHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 支付成功处理逻辑
 *
 * @author Alay
 * @since 2025-09-17 19:03
 */
@Component
@RequiredArgsConstructor
public class DefaultPaySuccessHandler implements XPaySuccessHandler {

    private final TradeService tradeService;
    private final TradeTypeHandler tradeTypeHandler;
    private final TradeNotifyService tradeNotifyService;

    @Override
    public void onSuccess(XPayTradeModel tradeModel, XPayResult<?> payResult) {
        // 更新交易单
        this.updateTrade(tradeModel, payResult);
        // 发送订单支付成功通知
        tradeTypeHandler.handler(tradeModel, payResult);
    }

    /**
     * 交易单表状态处理
     */
    private void updateTrade(XPayTradeModel tradeModel, XPayResult<?> payResult) {
        // 数据库中的交单信息
        Trade trade = tradeService.getById(tradeModel.tradeNo());
        if (TradeStateEnum.PAID == trade.getState()) {
            return;
        }
        Trade newTrade = trade.withNo()
                // 状态变更为已支付
                .state(TradeStateEnum.PAID)
                // 成功时间
                .successTime(LocalDateTime.now());
        // 支付回调表处理
        this.syncTradeNotifyInfo(tradeModel, payResult);
        // 写库
        tradeService.updateById(newTrade);
    }


    /**
     * 交易单回调表处理
     */
    private void syncTradeNotifyInfo(XPayTradeModel tradeModel, XPayResult<?> payResult) {
        if (tradeModel.channelKey().contains(XPayChannelEnum.BALANCE.channel())) {
            // 余额支付无需回调数据
            return;
        }
        // 交易单回调表数据
        TradeNotify tradeNotify = TradeNotify.builder(tradeModel.tradeNo(), payResult.outTradeNo())
                .notifyTime(payResult.time().toString());
        // 回调或者主动查询结果填充
        String dataStr = IJsonUtil.toJson(payResult.data());
        tradeNotify = payResult.way() == XPayResult.WAY_NOTIFY ? tradeNotify.request(dataStr) : tradeNotify.response(dataStr);
        tradeNotifyService.save(tradeNotify);
    }

}
