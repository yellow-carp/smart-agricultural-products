package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.io.Serial;
    import java.math.BigDecimal;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.baomidou.mybatisplus.annotation.TableLogic;
    import com.baomidou.mybatisplus.annotation.Version;
import java.time.LocalDateTime;

/**
* 商品SPU表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "商品SPU表")
@TableName(value = "pd_product_spu")
public class ProductSpu extends Model<ProductSpu>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 商品主键spu
    */
    @TableId
    @Schema(description = "商品主键spu")
    private String spu;
    /**
    * 供应商Id
    */
    @Schema(description = "供应商Id")
    private String supplierId;
    /**
    * 一级分类Id
    */
    @Schema(description = "一级分类Id")
    private String cat1Id;
    /**
    * 二级分类Id
    */
    @Schema(description = "二级分类Id")
    private String cat2Id;
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
    private Long unitId;
    /**
    * 包装id
    */
    @Schema(description = "包装id")
    private Long packagingId;
    /**
    * 产区id
    */
    @Schema(description = "产区id")
    private String areaId;
    /**
    * 状态：1-草稿，2-审核中，3-未通过，4-审核通过，5-下架，6-上架
    */
    @Schema(description = "状态：1-草稿，2-审核中，3-未通过，4-审核通过，5-下架，6-上架")
    private Integer state;
    /**
    * sku数量
    */
    @Schema(description = "sku数量")
    private Integer countSku;
    /**
    * 销量
    */
    @Schema(description = "销量")
    private Integer countSale;
    /**
    * 版本乐观锁
    */
    @Version
    @Schema(description = "版本乐观锁", hidden=true)
    private Integer version;
    /**
    * 操作者
    */
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "操作者", hidden=true)
    private String upsertBy;
    /**
    * 逻辑删除
    */
    @JsonIgnore
    @TableLogic
    @TableField(select = false)
    @Schema(description = "逻辑删除", hidden=true)
    private Boolean isDelete;
    /**
    * 修改时间
    */
    @Schema(description = "修改时间", hidden=true)
    private LocalDateTime updateTime;
    /**
    * 创建时间
    */
    @Schema(description = "创建时间", hidden=true)
    private LocalDateTime createTime;

}