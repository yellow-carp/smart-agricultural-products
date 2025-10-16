package com.xtechcn.cloud.payment.core.handler;

import cn.hutool.core.util.StrUtil;
import com.xtechcn.cloud.payment.constants.TradeStateEnum;
import com.xtechcn.cloud.payment.entity.Trade;
import com.xtechcn.cloud.payment.service.TradeService;
import com.xtechcn.commom.payment.constants.XPayConstants;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.model.XPayNotify;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.common.serialization.lang.JsonObj;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付失败处理逻辑
 *
 * @author Alay
 * @since 2025-09-17 19:07
 */
@Component
@RequiredArgsConstructor
public class DefaultPayFailureHandler implements XPayFailureHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TradeService tradeService;

    @Override
    public void onFailure(XPayTradeModel tradeModel, XPayException ex) {
        // 交易单数据同步
        this.updateTrade(tradeModel, ex);
        // 通知调用者
        this.failureNotify(tradeModel, ex);
    }

    /**
     * 更新处理交易单
     */
    private void updateTrade(XPayTradeModel tradeModel, XPayException ex) {
        // 数据库中的交单信息
        Trade trade = tradeService.getById(tradeModel.tradeNo());
        if (TradeStateEnum.NOT_PAY != trade.getState()) {
            return;
        }
        // 状态变更为已支付
        trade.setState(TradeStateEnum.CLOSED);

        // 消息信息
        trade.setMessage(ex.getMessage());
        if (null != ex.getMessage() && ex.getMessage().length() > 255) {
            String message = ex.getMessage().substring(0, 254);
            JsonObj extra = new JsonObj();
            extra.put(XPayConstants.ORDERS, message);
            trade.setExtra(extra);
        }
        // 写库
        tradeService.updateById(trade);
    }


    /**
     * 失败通知支付调用者
     */
    private void failureNotify(XPayTradeModel tradeModel, XPayException ex) {
        // 通知支付未成功的调用者
        XPayNotify xPayNotify = tradeModel.notifyUrl();
        if (StrUtil.isNotBlank(xPayNotify.failureUrl())) {
            // 发送回调通知
            logger.info("支付未成功,通知支付调用的地方, 交易单号为：" + tradeModel.tradeNo());

            // 请求体参数
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(XPayConstants.TRADE_NO_KEY, tradeModel.tradeNo());
            parameters.put(XPayConstants.MESSAGE_KEY, ex.getMessage());

            // 请求头
            Map<String, String> headers = new HashMap<>();
        }
    }
}
