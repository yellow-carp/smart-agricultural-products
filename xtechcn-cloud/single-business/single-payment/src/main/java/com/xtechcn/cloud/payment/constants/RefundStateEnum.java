package com.xtechcn.cloud.payment.constants;

import com.xtechcn.common.mp.enums.MpEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款状态
 *
 * @author Alay
 * @since 2023-10-09 15:28
 */
@Getter
@AllArgsConstructor
public enum RefundStateEnum implements MpEnum {

    /**
     * 受理中
     */
    ACCEPTING(1),

    /**
     * 退款状态：已退款
     */
    REFUNDED(3),
    /**
     * 拒绝退款
     */
    REJECT(4),
    /**
     * 退款失败
     */
    FAILED(5),
    /**
     * 退款失败回滚
     */
    ROLLBACK(9);


    private final int code;
}
