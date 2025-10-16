package com.xtechcn.cloud.customer.constants;

import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.mp.enums.MpEnum;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author Hanjie
 * @since 2025-09-17 09:18
 */
@AllArgsConstructor
public enum AreaTypeEnum implements MpEnum {

    AREA_ORIGIN(1, "产区"),
    AREA_SALES(2, "销区");

    private final int code;
    private final String desc;

    public boolean eq(AreaTypeEnum state) {
        return this == state;
    }

    public boolean notEq(AreaTypeEnum state) {
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

    public static AreaTypeEnum codeOf(int code) {
        AreaTypeEnum[] values = AreaTypeEnum.values();
        for (AreaTypeEnum value : values) {
            if (value.getCode() == code) return value;
        }
        throw new ParamException("state code not found");
    }
}
