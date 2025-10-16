package com.xtechcn.cloud.payment.constants;

import com.xtechcn.common.mp.enums.MpEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易支付状态
 *
 * @author Alay
 * @since 2022-05-19 12:57
 */
@Getter
@AllArgsConstructor
public enum TradeStateEnum implements MpEnum {
    /**
     * 未付款
     */
    NOT_PAY(1),
    /**
     * 已付款
     */
    PAID(2),
    /**
     * 付款状态：已退款
     */
    REFUND(3),
    /**
     * 支付关闭
     */
    CLOSED(9);

    private final int code;

}
