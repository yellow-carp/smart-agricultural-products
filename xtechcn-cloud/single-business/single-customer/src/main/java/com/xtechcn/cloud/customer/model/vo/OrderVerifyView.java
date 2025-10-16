package com.xtechcn.cloud.customer.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author hanjie
 * @since 2025-9-18 17:32:45
 */
@Getter
@Setter
public class OrderVerifyView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @Schema(description = "订单编号")
    private String orderNo;
    /**
     * 验证地址
     */
    @Schema(description = "支付交易单验证链接")
    private String verifyUrl;

    public OrderVerifyView orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public OrderVerifyView verifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
        return this;
    }
}
