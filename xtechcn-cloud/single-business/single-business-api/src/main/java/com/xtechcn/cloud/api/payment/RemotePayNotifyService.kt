package com.xtechcn.cloud.api.payment

/**
 * 远程支付通知服务
 *
 * @author Alay
 * @since 2025-09-10 17:11
 */
interface RemotePayNotifyService {
    /**
     * 支付前验证请求
     */
    fun preTradeVerify(parameters: Map<String, Any>?): Map<String, Any>? {
        return null
    }

    /**
     * 支付成功回调通知
     */
    fun successPayNotify(parameters: Map<String, Any>) {
    }

    /**
     * 支付失败回调通知
     */
    fun failedPayNotify(parameters: Map<String, Any>) {
    }

    /**
     * 退款成功回调通知
     */
    fun successRefundNotify(parameters: Map<String, Any>) {
    }

    /**
     * 退款成功回调通知
     */
    fun failedRefundNotify(parameters: Map<String, Any>) {
    }

}