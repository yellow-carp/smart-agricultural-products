package com.xtechcn.cloud.customer.constants;

import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.mp.enums.MpEnum;
import lombok.AllArgsConstructor;

/**
 * @author chengzuo
 * @since 2024-04-22 14:21
 */
@AllArgsConstructor
public enum PrOrderStateEnum implements MpEnum {

    TO_BE_PAID(1, "待支付"),
    PAYMENT_LOCK(2, "支付锁定"),
    CANCELED(3, "已取消"),
    PAYED(4, "已支付"),
    ;

    private final int code;
    private final String desc;

    public boolean eq(PrOrderStateEnum state) {
        return this == state;
    }

    public boolean notEq(PrOrderStateEnum state) {
        return this != state;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.desc;
    }


    public static PrOrderStateEnum codeOf(int code) {
        PrOrderStateEnum[] values = PrOrderStateEnum.values();
        for (PrOrderStateEnum value : values) {
            if (value.getCode() == code) return value;
        }
        throw new ParamException("state code not found");
    }

}