package com.xtechcn.cloud.payment.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易类型
 *
 * @author Alay
 * @since 2023-05-31 17:44
 */
@Getter
@AllArgsConstructor
public enum TradeTypeEnum {
    /**
     * 商品交易
     */
    PRODUCT(1),

    /**
     * 确认收款
     */
    CONFIRM(2),
    /**
     * 充值交易
     */
    CHARGE(3),
    /**
     * 转账交易
     */
    TRANSFER(4),
    /**
     * 线下支付交易
     */
    OFFLINE(5),

    /**
     * 购买会员
     */
    MEMBERSHIP(6),
    /**
     * 团购分账
     */
    GROUP_DIVIDE(7);
    private final int code;


    public static TradeTypeEnum codeOf(int code) {
        for (TradeTypeEnum value : TradeTypeEnum.values()) {
            if (code == value.getCode()) {
                return value;
            }
        }
        return TradeTypeEnum.PRODUCT;
    }

}
