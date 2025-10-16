package com.xtechcn.cloud.customer.constants;

import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.mp.enums.MpEnum;
import lombok.AllArgsConstructor;

/**
 * @author hanjie
 * @since 2025-9-17 11:23:44
 */
@AllArgsConstructor
public enum PurchaserStateEnum implements MpEnum {

    EDITING(1, "未入驻"),
    AUDIT(2, "已入驻"),
    ;

    private final int code;
    private final String desc;

    public boolean eq(PurchaserStateEnum state) {
        return this == state;
    }

    public boolean notEq(PurchaserStateEnum state) {
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

    public static PurchaserStateEnum codeOf(int code) {
        PurchaserStateEnum[] values = PurchaserStateEnum.values();
        for (PurchaserStateEnum value : values) {
            if (value.getCode() == code) return value;
        }
        throw new ParamException("state code not found");
    }

}