package com.xtechcn.cloud.payment.constants;

/**
 * 属性Key 名称常量
 *
 * @author Alay
 * @since 2024-04-30 16:16
 */
public interface AttributeKeyNames {
    /**
     * 微信 mchId
     */
    String WX_MCHID = "mchid";
    /**
     * 微信/支付宝 app ID
     */
    String APP_ID = "appId";
    /**
     * 微信 openid
     */
    String WX_OPEN_ID = "openid";
    /**
     * 微信付款方ID
     */
    String WX_PREPAY_ID = "prepay_id";
    /**
     * 扫码支付类型(中金)
     */
    String SCAN_PAYMENT_TYPE = "scanPaymentType";
    /**
     * 中金租户账户号
     */
    String ACCT_NO = "acctNo";

    // 字段相关
    String REFUND_AMOUNT = "refundAmount";
    String CREATE_TIME = "createTime";
    String UPDATE_TIME = "updateTime";
    String SUCCESS_TIME = "successTime";
    String CREATE_BY = "createBy";
    String STATE = "state";

    /**
     * 支付密码
     */
    String PASSWORD = "password";
    /**
     * 退款方式
     */
    String REFUND_TYPE_KEY = "refund_type";
    /**
     * CPCN 小程序标识
     */
    String PLUGIN_FLAG = "pluginFlag";

}
