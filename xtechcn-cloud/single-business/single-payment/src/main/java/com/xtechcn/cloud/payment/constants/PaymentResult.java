package com.xtechcn.cloud.payment.constants;

import com.xtechcn.common.core.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应结果
 *
 * @author Alay
 * @since 2022-05-20 14:40
 */
@Getter
@AllArgsConstructor
public enum PaymentResult implements BaseResult {

    /**
     * 请求成功时响应结果
     */
    BALANCE_NOT_SUFFICIENT("PE0001", "余额不足"),
    CHANNEL_UN_SUPPORT_CHARGE("PE0100", "支付渠道不支持充值"),

    TRADE_STATE_INVALID("PE0200", "交易当状态不符"),

    REFUND_STATE_INVALID("PE0300", "退款状态不符"),
    REFUND_AMOUNT_GT("PE0301", "退款金额不可大于最大可退款金额"),

    ACCOUNT_NOT_PHOTO("PE0400", "开户未进行影印件上送"),
    ACCOUNT_NOT_OPEN("PE0401", "您还未完成支付账户开设"),
    ACCOUNT_NOT_MATCH("PE0401", "开户类型不符合影印件中申请类型"),

    ACCOUNT_BIND_FAILED("PE0500", "绑卡校验失败，请重新确认绑卡"),
    ACCOUNT_BIND_WRONG_AMOUNT("PE0501", "打款金额错误，请重新填写"),

    AGENCY_NOT_AUTH("AG0501", "改用户没有线上签约权限"),

    FAILED_TO_RESET_PASSWORD("RP0601", "重置密码验证不通过"),
    CHANGE_PASSWORD_NOT_SET("RP0602", "零钱密码未设置"),

    TRANSFER_EXISTS_PROCESSING("RT0701", "存在处理中的提现记录，请稍候重试"),

    ;
    /**
     * 响应码
     */
    private final String code;
    /**
     * 响应消息
     */
    private final String message;


}
