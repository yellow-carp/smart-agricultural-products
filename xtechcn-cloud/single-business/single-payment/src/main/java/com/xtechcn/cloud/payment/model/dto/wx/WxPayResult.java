package com.xtechcn.cloud.payment.model.dto.wx;

import com.xtechcn.commom.payment.model.DefaultXPayResult;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 微信支付返回值
 *
 * @author Alay
 * @since 2023-07-25 15:34
 */
@Getter
@Setter
public class WxPayResult extends DefaultXPayResult<WxPayResult.Data> {

    @Override
    public String outTradeNo() {
        if (this.data() == null) return null;
        return this.data().getOut_trade_no();
    }

    public static  Builder<Data, WxPayResult> builder() {
        return new DefaultXPayResult.Builder<>(WxPayResult::new);
    }

    /**
     * 微信支付回调数据体
     *
     * @author Alay
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_5.shtml">微信文档</a>
     * @since 2023-05-07 09:42
     */
    @Getter
    @Setter
    public static class Data implements Serializable {
        private String transaction_id;
        private Amount amount;
        private String mchid;
        private String trade_state;
        private String bank_type;
        private List<Promotion> promotion_detail;
        private String success_time;
        private Payer payer;
        /**
         * 调用方的交易单号
         */
        private String out_trade_no;
        private String appid;
        private String trade_state_desc;
        private String trade_type;
        private String attach;

        @Getter
        @Setter
        public static class Amount {
            private String currency;
            private String payer_currency;
            private BigDecimal payer_total;
            private BigDecimal total;
        }

        @Getter
        @Setter
        public static class Payer {
            private String openid;
        }


        public static class Promotion {

        }

    }

}
