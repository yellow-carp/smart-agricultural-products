package com.xtechcn.cloud.product.model.po;

import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 生成订单参数PO
 *
 * @author Dong
 * @date 2023-04-19 15:10
 */
@Getter
@Setter
public class OrderSkuParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * sku
     */
    @Schema(description = "sku")
    private String sku;

    /**
     * 购买数量
     */
    @Schema(description = "购买数量")
    private Integer num;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;
}
