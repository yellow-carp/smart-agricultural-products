package com.xtechcn.cloud.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 管理后台 商品SPU视图模型
 *
 * @author Weijixiao
 * @since 2025-09-17 11:34
 */

@Getter
@Setter
public class ProductSpuModel {
    /**
     * 商品spu
     */
    @Schema(description = "商品spu")
    private String spu;
    /**
     * 销售市场
     */
    @Schema(description = "销售市场")
    private String areaId;
    /**
     * 供应商Id
     */
    @Schema(description = "供应商Id")
    private String supplierId;
    /**
     * 三级分类Id
     */
    @Schema(description = "三级分类Id")
    private String cat3Id;
    /**
     * 商品标题
     */
    @Schema(description = "商品标题")
    private String title;
    /**
     * 商品主图片
     */
    @Schema(description = "商品主图片")
    private String poster;
    /**
     * 视频连接
     */
    @Schema(description = "视频连接")
    private String video;
    /**
     * 计量单位
     */
    @Schema(description = "计量单位")
    private long unit;
    // /**
    //  * 税率
    //  */
    // @Schema(description = "税率")
    // private IPercent taxRate;

}