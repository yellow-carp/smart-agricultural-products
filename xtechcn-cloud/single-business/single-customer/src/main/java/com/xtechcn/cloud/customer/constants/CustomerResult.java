package com.xtechcn.cloud.customer.constants;

import com.xtechcn.common.core.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 *
 * @author Alay
 * @since 2025-08-31 16:32
 */
@Getter
@AllArgsConstructor
public enum CustomerResult implements BaseResult {

    STATE_UNABLE_OPERATE("CU0100", "当前状态无法操作"),
    OPENID_NOT_NULL("CU0101", "OpenId不能为空"),
    USERID_NOT_NULL("CU0102", "用户Id不能为空"),
    CONFIG_NOT_FOUND("CU0103", "未配置采购商邀请码，请联系管理员"),
    INVALID_INVITATION_CODE("CU0104", "无效的邀请码"),
    BIND_YOUR_MOBILE_PHONE_NUMBER_FIRST("CU0105", "请先绑定手机号"),
    ORDER_PAY_ERROR("CU0106", "订单重复支付,请稍后重试!"),

    ;
    private final String code;
    private final String message;
}
