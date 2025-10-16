package com.xtechcn.cloud.product.constants;

import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.mp.enums.MpEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品状态
 *
 * @author Weijixiao
 * @since 2025-09-17 14:51
 */
@Getter
@AllArgsConstructor
public enum ProductStateEnum implements MpEnum {

    DRAFT(1, "草稿"),
    CHECKING(2, "审核中"),
    REJECTED(3, "未通过"),
    PASSED(4, "审核通过"),
    DOWN(5, "下架"),
    UP(6, "上架");

    private final int code;
    private final String desc;

    public static ProductStateEnum codeOf(int code) {
        ProductStateEnum[] values = ProductStateEnum.values();
        for (ProductStateEnum value : values) {
            if (value.getCode() == code) return value;
        }
        throw new ParamException("state code not found");
    }
}
