package com.xtechcn.cloud.product.model;

import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 商品SKU 数据
 *
 * @author Weijixiao
 * @since 2025-09-17 11:41
 */
@Getter
@Setter
public class ProductSkuModel {
    /**
     * SKU编码
     */
    @Schema(description = "SKU编码")
    private String sku;
    /**
     * 商品主图片
     */
    @Schema(description = "商品主图片")
    private String poster;
    /**
     * 条形码
     */
    @Schema(description = "条形码")
    @Length(max = 127, message = "条形码不能超过127个字符")
    private String upc;
    /**
     * 含税单价(分)
     */
    @Schema(description = "含税单价(分)")
    private MoneyPenny unitPrice;
    /**
     * 税额(分)
     */
    @Schema(description = "税额(分)")
    private MoneyPenny taxAmount;
    /**
     * 市场价(分)
     */
    @Schema(description = "市场价(分)")
    private MoneyPenny marketPrice;
    /**
     * 库存
     */
    @Schema(description = "库存数量")
    private Integer inventory;
    /**
     * 重量（单位：kg）
     */
    @Schema(description = "重量（单位：kg）")
    private Double weight;

    public Boolean isEffective() {
        // 是否是一个有效的商品
        return null != this.unitPrice && this.unitPrice.gt(MoneyPenny.ZERO);
    }

}
