package com.xtechcn.cloud.customer.model;

import com.xtechcn.common.core.lang.MoneyPenny;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商品支付交易订单
 *
 * @author Alay
 * @since 2023-08-01 14:09
 */
@Getter
@Setter
public class TradeCheckModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 交易单号
     */
    private String tradeNo;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 支付渠道配置唯一标识
     */
    private String channelKey;
    /**
     * 付款方
     */
    private String payer;

    /**
     * 交易单主题信息
     */
    private String subject;
    /**
     * 商品主题信息
     */
    private String body;
    /**
     * 回调地址相关信息
     */
    private PayNotify notifyUrl;
    /**
     * 交易单类型: 1-商品交易, 2-确认收款, 3-充值交易, 4-转账交易
     */
    private Integer tradeType;
    /**
     * 扩展信息
     */
    private String attach;
    /**
     * 更多属性
     */
    private final Map<String, Object> attributes = new ConcurrentHashMap<>(1 << 4);

    /**
     * 交易订单
     */
    private List<TradeOrder> orders;


    @Getter
    @Setter
    public static class TradeOrder {
        /**
         * 订单编号
         */
        private String orderNo;
        /**
         * 订单类型
         */
        private Integer orderType;
        /**
         * 收款方
         */
        private String payee;
        /**
         * 订单金额
         */
        private MoneyPenny amount;
        /**
         * 付款金额
         */
        private MoneyPenny payAmount;
    }


    @Setter
    @Getter
    public static class PayNotify {
        /**
         * 支付前参数检验地址
         */
        private String verifyUrl;
        /**
         * 支付成功回调地址
         */
        private String successUrl;
        /**
         * 失败回调地址
         */
        private String failureUrl;

        /**
         * 跳转页面的地址
         */
        private String returnUrl;
        /**
         * 第三方支付回调地址
         */
        private String callbackUrl;
        /**
         * 分账信息查询接口
         */
        private String splitInquiryUrl;
    }
}
