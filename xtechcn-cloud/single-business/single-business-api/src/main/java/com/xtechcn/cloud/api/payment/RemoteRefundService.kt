package com.xtechcn.cloud.api.payment;

import com.xtechcn.cloud.api.payment.model.RemoteRefundModel;

/**
 * 退款服务
 *
 * @author Alay
 * @since 2025-09-10 18:26
 */
interface RemoteRefundService {
    /**
     * 退款
     */
    fun refund(refundModel: RemoteRefundModel)
}
