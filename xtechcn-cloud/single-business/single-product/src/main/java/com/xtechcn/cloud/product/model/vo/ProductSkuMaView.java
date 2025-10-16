package com.xtechcn.cloud.product.model.vo;

import com.xtechcn.cloud.product.model.ProductSkuModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 管理后台 商品SKU视图模型
 *
 * @author Weijixiao
 * @since 2025-09-17 11:40
 */
@Getter
@Setter
public class ProductSkuMaView extends ProductSkuModel {
    /**
     * 销售属性组合标识
     */
    @Schema(description = "销售属性组合标识")
    private String saleAttrs;
    /**
     * 销量
     */
    @Schema(description = "销量")
    private Integer countSale;
}
