package com.xtechcn.cloud.product.entity;

import com.xtechcn.common.mybatis.wrapper.SqlCondition;
import com.xtechcn.common.mybatis.wrapper.SqlIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Weijixiao
 * @since 2025-09-17 11:24
 */
@Getter
@Setter
public class ProductQueryParam {
    /**
     * 商品主键spu
     */
    @Schema(description = "商品主键spu")
    @SqlCondition(type = SqlCondition.Type.LIKE)
    private String spu;
    /**
     * 商品精确匹配spu
     */
    @Schema(description = "商品精确匹配spu")
    @SqlCondition(type = SqlCondition.Type.EQ, column = "spu")
    private String spuExact;
    /**
     * 商品SKU
     */
    @SqlIgnore
    @Schema(description = "商品SKU")
    private String sku;
    /**
     * 品牌Id
     */
    @Schema(description = "品牌Id")
    private Integer brandId;
    /**
     * 一级分类Id
     */
    @Schema(description = "一级分类Id")
    private Integer cat1Id;
    /**
     * 二级分类Id
     */
    @Schema(description = "二级分类Id")
    private Integer cat2Id;
    /**
     * 三级分类Id
     */
    @Schema(description = "三级分类Id")
    private Integer cat3Id;
    /**
     * 商品标题
     */
    @SqlCondition(type = SqlCondition.Type.LIKE)
    @Schema(description = "商品标题")
    private String title;
    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer state;
    /**
     * 是否大单议价 1 是；0 否
     */
    @SqlIgnore
    @Schema(description = "是否大单议价")
    private Boolean isInquiry;
    /**
     * 操作人(最后操作人)
     */
    @SqlCondition(column = "upsert_by")
    @Schema(description = "操作人(最后操作人)")
    private String userId;
    /**
     * 开始时间
     */
    @Schema(description = "开始时间(日)")
    private LocalDate startTime;
    /**
     * 结束时间
     */
    @Schema(description = "结束时间(日)")
    private LocalDate endTime;
}
