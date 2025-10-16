package com.xtechcn.cloud.product.model.po;

import com.xtechcn.cloud.product.model.ProductSpuModel;
import com.xtechcn.cloud.product.model.SaleAttrModel;
import com.xtechcn.cloud.product.model.SpecAttrModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 商品SPU参数对象
 * @author Weijixiao
 * @since 2025-09-17 14:34
 */
public class ProductSpuMaParam extends ProductSpuModel {
    /**
     * 商品SKU
     */
    @Getter
    @Setter
    @Schema(description = "商品SKU")
    private List<ProductSkuMaParam> productSkus;
    /**
     * 销售属性
     */
    @Getter
    @Setter
    @Schema(description = "销售属性")
    private List<SaleAttrModel> saleAttrs;
    /**
     * 规格属性
     */
    @Getter
    @Setter
    @Schema(description = "规格属性")
    private List<SpecAttrModel> specAttrs;
    /**
     * 详情富文本信息
     */
    @Getter
    @Setter
    @Schema(description = "详情富文本信息")
    private String details;
    /**
     * 商品多图
     */
    @Getter
    @Setter
    @Schema(description = "商品多图")
    private List<String> images;
    /**
     * 运费模板Id
     */
    @Getter
    @Setter
    @Schema(description = "运费模板Id")
    private Integer freightId;
    /**
     * 最小购买
     */
    @Getter
    @Setter
    @Schema(description = "最小购买")
    private Integer limitMin;
    /**
     * 是否仅展示：0-否，1-是。
     */
    @Getter
    @Setter
    @Schema(description = "是否仅展示：0-否，1-是。")
    private Boolean isShowOnly = false;
    /**
     * 是否公开 0-不公开 1-公开
     */
    @Getter
    @Setter
    @Schema(description = "是否公开 0-不公开 1-公开")
    private Boolean isPublic = true;
    /**
     * 二级单位 id，多个以逗号隔开
     */
    @Getter
    @Setter
    @Schema(description = "二级单位 id，多个以逗号隔开")
    private Set<String> deptIds;
    /**
     * 税码
     */
    @Getter
    @Setter
    @Schema(description = "税码")
    private String taxCode;
    /**
     * 自动上架
     */
    @Getter
    @Setter
    @Schema(description = "自动自动上架")
    private Boolean autoUp = false;

    /**
     * 是否是添加
     */
    @Schema(description = "是否是添加", hidden = true)
    private Boolean isAdd;
    /**
     * 需要删除的SKU
     */
    @Schema(description = "需要删除的SKU", hidden = true)
    private Set<String> removeSkus;

    public boolean isAdd() {
        if (null != isAdd) return isAdd;
        this.isAdd = null == this.getSpu();
        return this.isAdd;
    }

    public boolean isSingleSku() {
        return 1 == this.getProductSkus().size();
    }

    public ProductSkuMaParam singleSku() {
        return this.getProductSkus().getFirst();
    }

    public void removeSkus(Set<String> removeSkus) {
        this.removeSkus = removeSkus;
    }

    public Set<String> removeSkus() {
        return removeSkus;
    }

    public void addProductSku(ProductSkuMaParam productSkuMaParam) {
        this.productSkus.add(productSkuMaParam);
    }

    public static ProductSpuMaParam excelImport() {
        ProductSpuMaParam productSpuMaParam = new ProductSpuMaParam();
        productSpuMaParam.productSkus = new ArrayList<>();
        productSpuMaParam.isAdd = true;
        return productSpuMaParam;
    }
}
