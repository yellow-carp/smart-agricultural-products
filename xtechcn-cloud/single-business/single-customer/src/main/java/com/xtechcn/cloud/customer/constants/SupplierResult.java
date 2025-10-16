package com.xtechcn.cloud.customer.constants;

import com.xtechcn.common.core.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Weijixiao
 * @since 2025-09-16 17:43
 */
@Getter
@AllArgsConstructor
public enum SupplierResult implements BaseResult {

    SUPPLIER_DATA("E00001", "供应商数据异常"),
    SUPPLIER_STATE_ERROR("E00002","供应商状态异常" ),
    SUPPLIER_NOT_EXIST("E00002","供应商不存在" );

    private final String code;
    private final String message;
}
