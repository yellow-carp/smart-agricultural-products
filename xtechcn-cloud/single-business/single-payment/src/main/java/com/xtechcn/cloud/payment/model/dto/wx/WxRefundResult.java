package com.xtechcn.cloud.payment.model.dto.wx;

import com.xtechcn.commom.payment.model.DefaultXPayResult;
import com.xtechcn.common.core.lang.MoneyPenny;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Alay
 * @since 2024-06-21 14:34
 */
public class WxRefundResult extends DefaultXPayResult<WxRefundResult.RefundData> {

    public static DefaultXPayResult.Builder<RefundData, WxRefundResult> builder() {
        return new DefaultXPayResult.Builder<>(WxRefundResult::new);
    }

    @Getter
    @Setter
    public static class RefundData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        /*
        {
            "mchid": "1642979517",
            "out_trade_no": "1804035435951255552",
            "transaction_id": "4200002310202406216046417221",
            "out_refund_no": "1804035435951255552",
            "refund_id": "50302710182024062150770823248",
            "refund_status": "SUCCESS",
            "success_time": "2024-06-21T14:18:24+08:00",
            "amount": {
                "total": 1,
                "refund": 1,
                "payer_total": 1,
                "payer_refund": 1
            },
            "user_received_account": "支付用户零钱"
        }
         */
        private String mchid;
        private String out_trade_no;
        private String transaction_id;
        private String out_refund_no;
        private String refund_id;
        private String refund_status;
        private String success_time;
        private String create_time;
        private String status;
        private String funds_account;
        private Amount amount;
        private List<Promotion> promotion_detail;
        private String user_received_account;

        @Getter
        @Setter
        public static class Amount {
            private MoneyPenny total;
            private MoneyPenny refund;
            private List<From> from;
            private MoneyPenny payer_total;
            private MoneyPenny payer_refund;
            private MoneyPenny settlement_refund;
            private MoneyPenny settlement_total;
            private MoneyPenny discount_refund;
            private String currency;
        }

        public static class Promotion {

        }

        public static class From {

        }

    }
}
