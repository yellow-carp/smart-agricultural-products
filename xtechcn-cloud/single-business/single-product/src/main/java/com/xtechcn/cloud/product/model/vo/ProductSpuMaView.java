package com.xtechcn.cloud.product.model.vo;

import com.xtechcn.cloud.product.model.ProductSpuModel;
import com.xtechcn.cloud.product.model.SaleAttrModel;
import com.xtechcn.cloud.product.model.SpecAttrModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 商品详情
 *
 * @author Weijixiao
 * @since 2025-09-17 11:34
 */
@Setter
@Getter
public class ProductSpuMaView extends ProductSpuModel {
    /**
     * 分类名称
     */
    private CategoryView category;
    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer state;
    /**
     * 商品SKU
     */
    @Schema(description = "商品SKU")
    private List<ProductSkuMaView> productSkus;
    /**
     * 详情富文本信息
     */
    @Schema(description = "详情富文本信息")
    private String details;
    /**
     * 商品多图
     */
    @Schema(description = "商品多图")
    private List<String> images;
    /**
     * 审核信息
     */
    @Schema(description = "审核信息")
    private String checkMsg;
    /**
     * 运费模板Id
     */
    @Schema(description = "运费模板Id")
    private Integer freightId;
    /**
     * 最小购买
     */
    @Schema(description = "最小购买")
    private Integer limitMin;
    /**
     * 是否仅展示：0-否，1-是。
     */
    @Schema(description = "是否仅展示：0-否，1-是。")
    private Boolean isShowOnly;
    /**
     * 是否公开 0-不公开 1-公开
     */
    @Schema(description = "是否公开 0-不公开 1-公开")
    private Boolean isPublic;
    /**
     * 二级单位 id，多个以逗号隔开
     */
    @Schema(description = "二级单位 id，多个以逗号隔开")
    private Set<String> deptIds;
    /**
     * 税码
     */
    @Schema(description = "税码")
    private String taxCode;
    /**
     * 规格属性
     */
    @Schema(description = "规格属性")
    private List<SpecAttrModel> specAttrs;
    /**
     * 销售属性
     */
    @Schema(description = "销售属性")
    private List<SaleAttrModel> saleAttrs;

}
