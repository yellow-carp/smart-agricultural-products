package com.xtechcn.cloud.product.model.po;

import com.xtechcn.cloud.product.model.ProductSkuModel;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品SKU 参数
 *
 * @author Weijixiao
 * @since 2025-09-17 14:35
 */
public class ProductSkuMaParam extends ProductSkuModel {
    /**
     * spu编码
     */
    @Getter
    @Setter
    @Schema(description = "spu编码", hidden = true)
    private String spu;
    /**
     * 销售属性组合标识
     */
    @Getter
    @Setter
    @Schema(description = "销售属性组合标识")
    private String saleAttrs;
    /**
     * 排序
     */
    @Getter
    @Setter
    @Schema(description = "排序")
    private Integer sort;
    /**
     * 京东价格(分)
     */
    @Getter
    @Setter
    @Schema(description = "京东价格(分)")
    private MoneyPenny jdPrice;
    /**
     * 京东商品链接
     */
    @Getter
    @Setter
    @Schema(description = "京东商品链接")
    private String jdLink;
    /**
     * 供应商官网链接
     */
    @Getter
    @Setter
    @Schema(description = "供应商官网链接")
    private String owsLink;
    /**
     * 电建商城码
     */
    @Getter
    @Setter
    @Schema(description = "电建商城码")
    private String djCode;

    /**
     * 是否是添加
     */
    @Schema(description = "是否是添加", hidden = true)
    private Boolean isAdd;


    public ProductSkuMaParam spu(String spu) {
        this.spu = spu;
        return this;
    }

    /**
     * 在参数转换之前已经将其标记，添加或修改
     * com.xtechcn.cloud.dianjian.service.impl.ProductSkuServiceImpl#convertEntity(com.xtechcn.cloud.dianjian.model.po.ProductSkuMaParam)
     */
    public boolean isAdd() {
        if (null != isAdd) return isAdd;
        this.isAdd = null == this.getSku();
        return this.isAdd;
    }


    public ProductSkuMaParam isAdd(boolean add) {
        this.isAdd = add;
        return this;
    }

    public static ProductSkuMaParam excelImport() {
        ProductSkuMaParam skuParamModel = new ProductSkuMaParam();
        skuParamModel.isAdd = true;
        return skuParamModel;
    }

}
