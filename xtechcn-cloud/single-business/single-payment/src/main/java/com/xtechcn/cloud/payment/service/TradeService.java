package com.xtechcn.cloud.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.payment.entity.Trade;
import com.xtechcn.commom.payment.model.XPayRefundModel;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.repository.XPayTradeRepository;


/**
 * 交易信息表
 *
 * @author Alay
 * @since 2022-05-16 10:17:37
 */
public interface TradeService extends IService<Trade>, XPayTradeRepository {

    /**
     * 交易是否已经完成支付
     */
    boolean isPaid(String tradeNo);

    /**
     * 退款成功后处理
     */
    void refundSuccessful(XPayRefundModel refundModel);

    /**
     * 退款失败后处理
     */
    void refundUnsuccessful(XPayRefundModel refundModel, XPayResult<?> xPayResult);

    /**
     * 退款回滚(常规情况下不需要手动回滚,有从夫退款的风险)
     */
    void refundRollback(XPayRefundModel refundModel);

}
