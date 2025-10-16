package com.xtechcn.cloud.customer.constants;

import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.mp.enums.MpEnum;
import lombok.AllArgsConstructor;

/**
 * @author hanjie
 * @since 2025-9-17 11:23:44
 */
@AllArgsConstructor
public enum SupplierStateEnum implements MpEnum {

    EDITING(1, "编辑中"),
    AUDIT(2, "待审核"),
    PASS(3, "已通过"),
    REFUSE(4, "已拒绝");

    private final int code;
    private final String desc;

    public boolean eq(SupplierStateEnum state) {
        return this == state;
    }

    public boolean notEq(SupplierStateEnum state) {
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

    public static SupplierStateEnum codeOf(int code) {
        SupplierStateEnum[] values = SupplierStateEnum.values();
        for (SupplierStateEnum value : values) {
            if (value.getCode() == code) return value;
        }
        throw new ParamException("state code not found");
    }

}