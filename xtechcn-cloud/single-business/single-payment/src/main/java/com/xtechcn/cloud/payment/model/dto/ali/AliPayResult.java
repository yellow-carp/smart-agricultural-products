package com.xtechcn.cloud.payment.model.dto.ali;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.xtechcn.commom.payment.model.DefaultXPayResult;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 支付宝支付结果
 *
 * @author Alay
 * @since 2023-06-11 02:50
 */
@Getter
public class AliPayResult extends DefaultXPayResult<AliPayResult.Data> {
    /**
     * 查询时候数据字段
     */
    @Setter
    @JsonProperty("alipay_trade_query_response")
    private Data data;
    /**
     * 签名
     */
    private String sign;

    public AliPayResult sign(String sign) {
        this.sign = sign;
        return this;
    }

    @Override
    public String outTradeNo() {
        if (this.data == null) return null;
        return this.data.getOutTradeNo();
    }

    @Override
    public String getMessage() {
        return data.getMsg();
    }

    public static Builder<Data, AliPayResult> builder() {
        return new DefaultXPayResult.Builder<>(AliPayResult::new);
    }


    /**
     * 支付宝支付结果
     *
     * @author Alay
     * @since 2022-05-27 15:23
     */
    @Getter
    @Setter
    public static class Data {
        /**
         * 通知的发送时间
         */
        @JsonProperty(value = "notify_time")
        private String notifyTime;
        /**
         * 通知的类型
         */
        @JsonProperty(value = "notify_type")
        private String notifyType;
        /**
         * 通知校验ID
         */
        @JsonProperty(value = "notify_id")
        private String notifyId;
        /**
         * 通知的签名类型
         */
        @JsonProperty(value = "sign_type")
        private String signType;
        /**
         * 签名方式
         */
        private String sign;
        /**
         * 支付宝交易号
         */
        @JsonProperty(value = "trade_no")
        private String tradeNo;
        /**
         * 商户订单号
         */
        @JsonProperty(value = "out_trade_no")
        private String outTradeNo;
        /**
         * 交易状态
         */
        @JsonProperty(value = "trade_status")
        private String tradeStatus;
        /**
         * 订单金额
         */
        @JsonProperty(value = "total_amount")
        private String totalAmount;
        /**
         * 实收金额
         */
        @JsonProperty(value = "receipt_amount")
        private String receiptAmount;
        /**
         * 用户在交易中支付的可开发票的金额
         */
        @JsonProperty(value = "invoice_amount")
        private String invoiceAmount;
        /**
         * 该笔交易创建的时间 2023-08-09 11:31:07
         */
        @JsonProperty(value = "gmt_create")
        private String gmtCreate;
        /**
         * 编码格式
         */
        private String charset;
        /**
         * 该笔交易的买家付款时间
         */
        @JsonProperty(value = "gmt_payment")
        private String gmtPayment;
        /**
         * 商品的标题/交易标题/订单标题/订单关键字等
         */
        private String subject;
        /**
         * 买家支付宝账号对应的支付宝唯一用户号
         */
        @JsonProperty(value = "buyer_id")
        private String buyerId;
        /**
         * 公共回传参数
         */
        @JsonProperty(value = "passback_params")
        private String passbackParams;
        /**
         * 支付成功的各个渠道金额信息
         */
        @JsonProperty(value = "fund_bill_list")
        private String fundBillList;
        /**
         * 应用程序id
         */
        @JsonProperty(value = "app_id")
        private String appId;
        /**
         * 验证应用程序
         */
        @JsonProperty(value = "auth_app_id")
        private String authAppId;
        /**
         * 使用集分宝支付的金额。
         */
        @JsonProperty(value = "point_amount")
        private String pointAmount;
        /**
         * 用户在交易中支付的金额。
         */
        @JsonProperty(value = "buyer_pay_amount")
        private String buyerPayAmount;
        /**
         * 卖家支付宝用户号
         */
        @JsonProperty(value = "seller_id")
        private String sellerId;

        private String code;
        private String msg;

        @JsonProperty(value = "buyer_logon_id")
        private String buyerLogonId;

        @JsonProperty(value = "buyer_user_id")
        private String buyerUserId;

        @JsonProperty(value = "buyer_user_type")
        private String buyerUserType;
        /**
         * 账单信息
         */
        private List<FundBill> fundBills;

        public List<FundBill> getFundBills() {
            if (null != fundBillList) {
                this.fundBills = IJsonUtil.parseArray(fundBillList, new TypeReference<List<FundBill>>() {
                });
            }
            return fundBills;
        }

        @Getter
        @Setter
        public static class FundBill {
            private String amount;
            private String fundChannel;
        }
    /*
    {
        "notify_time":"2022-05-27 19:28:10",
        "notify_type":"trade_status_sync",
        "notify_id":"2022052700222192810017510519099833",
        "sign_type":"RSA2",
        "sign":"N5Si4fzvmgtPpW/xYYGCZCFatcIZ39TotrhUTGH9O8Xxaqhf1m0fC7It0AAYIBdFbITzsjoiDzyoCet0xatNQ8sjVQ+h65OTqKVQCrBPYknOlUdJZ+sJCxqI7QKN6TFGnTmcYstjtvgAuuLQsXLXfgiTH78s1QaQt1vopGni5kbxCgmj8YitGRSxvZ9w35o+BQ0dtuBARcdU1TzCn48+jUhcXY3lpAfRNDSPirEgqDrDicPgbJfcv//RKWhcRXMBCEX+cj/PBjkV0t/oq2JtpYxRHOJ7l7+wNBB+Q9jNpUAMkg2B5+txWIDpsWkqvQv8MNyLrNjenr8PKpSsAqRP+A==",
        "trade_no":"2022052722001417510501739427",
        "out_trade_no":"XT202205271927435591530148742059827201",
        "trade_status":"TRADE_SUCCESS",
        "total_amount":"6.99",
        "receipt_amount":"6.99",
        "invoice_amount":"6.99",
        "gmt_create":"2022-05-27 19:27:58",
        "charset":"UTF-8",
        "gmt_payment":"2022-05-27 19:28:09",
        "subject":"手电转",
        "buyer_id":"2088622958817510",
        "passback_params":"1",
        "fund_bill_list":"[{\"amount\":\"6.99\",\"fundChannel\":\"ALIPAYACCOUNT\"}]",
        "fund_bills":[
            {
                "amount":"6.99",
                "fund_channel":null
            }
        ],
        "app_id":"2021000119696782",
        "auth_app_id":"2021000119696782",
        "point_amount":"0.00",
        "buyer_pay_amount":"6.99",
        "seller_id":"2088621958747502",
        "code":null,
        "msg":null,
        "buyer_logon_id":null,
        "buyer_user_id":null,
        "buyer_user_type":null
    }
    */

    }


}
