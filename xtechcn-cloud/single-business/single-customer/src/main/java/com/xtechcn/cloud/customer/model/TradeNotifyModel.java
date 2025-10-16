package com.xtechcn.cloud.customer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 交易失败通知对象
 *
 * @author Alay
 * @since 2023-08-04 13:52
 */
@Getter
@Setter
public class TradeNotifyModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 退款唯一标识
     */
    private String outRefundNo;

    /**
     * 属性
     */
    private Map<String, Object> attributes;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 交易失败原因
     */
    private String message;

}
