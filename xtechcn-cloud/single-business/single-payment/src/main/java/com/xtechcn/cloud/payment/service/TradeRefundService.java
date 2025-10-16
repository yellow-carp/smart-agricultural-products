package com.xtechcn.cloud.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.api.payment.RemoteRefundService;
import com.xtechcn.cloud.payment.entity.TradeRefund;
import com.xtechcn.commom.payment.model.XPayRefundModel;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.repository.XPayRefundRepository;

import java.util.List;


/**
 * 交易退款表
 *
 * @author Alay
 * @since 2023-5-25 08:58:28
 */
public interface TradeRefundService extends IService<TradeRefund>, XPayRefundRepository, RemoteRefundService {
    /**
     * 通过交易单查询退款单信息
     */
    List<TradeRefund> listByTrade(String tradeNo);

    /**
     * 是否退款完成
     */
    boolean isPaid(String refundNo);

    /**
     * 是否已经退款
     */
    boolean isRefund(String refundNo);

    /**
     * 退款成功处理
     */
    void successful(XPayRefundModel refundModel, XPayResult<?> xPayResult);

    /**
     * 退款未成功
     */
    void unsuccessful(XPayRefundModel refundModel, XPayResult<?> xPayResult);

    /**
     * 退款回滚
     */
    boolean refundRollback(XPayRefundModel refundModel);

}
