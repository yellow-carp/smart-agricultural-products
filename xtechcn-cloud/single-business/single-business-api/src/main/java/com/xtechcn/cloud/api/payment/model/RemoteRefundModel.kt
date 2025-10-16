package com.xtechcn.cloud.api.payment.model

import com.xtechcn.common.core.lang.MoneyPenny
import com.xtechcn.common.core.lang.XAttributes
import kotlin.jvm.JvmStatic
import java.util.concurrent.ConcurrentHashMap

/**
 * 远程退款接口数据体
 *
 * @author Alay
 * @since 2025-09-11 16:55
 */
class RemoteRefundModel private constructor(
    /**
     * 交易单号
     */
    val tradeNo: String?,
    /**
     * 退款金额
     */
    val amount: MoneyPenny?,
    /**
     * 退款原因
     */
    val reason: String?,
    /**
     * 扩展数据
     */
    val _attributes: Map<String, Any>
) : XAttributes<String> {

    override fun attributes(): Map<String?, Any?>? {
        return this._attributes as Map<String?, Any?>?
    }

    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var tradeNo: String? = null
        private var amount: MoneyPenny? = null
        private var reason: String? = null
        private val attributes: MutableMap<String, Any> = ConcurrentHashMap()

        fun tradeNo(tradeNo: String) = apply { this.tradeNo = tradeNo }

        fun amount(amount: MoneyPenny) = apply { this.amount = amount }

        fun reason(reason: String) = apply { this.reason = reason }

        fun addAttribute(key: String?, value: Any?) = apply {
            if (key != null && value != null) {
                this.attributes[key] = value
            }
        }

        fun addAttributes(data: Map<String, Any>?) = apply {
            if (!data.isNullOrEmpty()) {
                this.attributes.putAll(data)
            }
        }

        fun build(): RemoteRefundModel {
            return RemoteRefundModel(tradeNo, amount, reason, HashMap(attributes))
        }
    }
}
