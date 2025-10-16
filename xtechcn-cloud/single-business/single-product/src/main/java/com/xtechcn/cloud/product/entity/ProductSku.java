package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* 商品SKU
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "商品SKU")
@TableName(value = "pd_product_sku")
public class ProductSku extends Model<ProductSku>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键id(sku)
    */
    @TableId
    @Schema(description = "主键id(sku)")
    private String sku;
    /**
    * 商品Spu
    */
    @Schema(description = "商品Spu")
    private String spu;
    /**
    * 分类Id(二级分类冗余)
    */
    @Schema(description = "分类Id(二级分类冗余)")
    private String cat3Id;
    /**
    * 商品主图片
    */
    @Schema(description = "商品主图片")
    private String poster;
    /**
    * 条形码
    */
    @Schema(description = "条形码")
    private String upc;
    /**
    * 含税单价(分)
    */
    @Schema(description = "含税单价(分)")
    private MoneyPenny unitPrice;
    /**
    * 市场价(分)
    */
    @Schema(description = "市场价(分)")
    private MoneyPenny marketPrice;
    /**
    * 是否促销
    */
    @Schema(description = "是否促销")
    private MoneyPenny isPromo;
    /**
    * 销量
    */
    @Schema(description = "销量")
    private Integer countSale;
    /**
    * 排序
    */
    @Schema(description = "排序")
    private Integer sort;
    /**
    * 库存数
    */
    @Schema(description = "库存数")
    private Integer quantity;
    /**
    * 乐观锁
    */
    @Version
    @Schema(description = "乐观锁", hidden=true)
    private Integer version;
    /**
    * 逻辑删除
    */
    @JsonIgnore
    @TableLogic
    @TableField(select = false)
    @Schema(description = "逻辑删除", hidden=true)
    private Boolean isDelete;
    /**
    * 操作者
    */
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "操作者", hidden=true)
    private String upsertBy;
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