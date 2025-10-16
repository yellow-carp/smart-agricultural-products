package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.io.Serial;
    import java.math.BigDecimal;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.baomidou.mybatisplus.annotation.TableLogic;
import java.time.LocalDateTime;

/**
* 购物车表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "购物车表")
@TableName(value = "pd_cart")
public class Cart extends Model<Cart>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * id
    */
    @TableId
    @Schema(description = "id", hidden=true)
    private String id;
    /**
    * 供应商id
    */
    @Schema(description = "供应商id")
    private String supplierId;
    /**
    * 产区id
    */
    @Schema(description = "产区id")
    private String areaId;
    /**
    * spu
    */
    @Schema(description = "spu")
    private String spu;
    /**
    * sku
    */
    @Schema(description = "sku")
    private String sku;
    /**
    * 商品名称
    */
    @Schema(description = "商品名称")
    private String title;
    /**
    * 商品主图片
    */
    @Schema(description = "商品主图片")
    private String poster;
    /**
    * 购买数量
    */
    @Schema(description = "购买数量")
    private Integer num;
    /**
    * 单位id
    */
    @Schema(description = "单位id")
    private Long unitId;
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
    * 促销优惠金额
    */
    @Schema(description = "促销优惠金额")
    private MoneyPenny saveAmount;
    /**
    * 是否有货
    */
    @Schema(description = "是否有货")
    private Boolean availability;
    /**
    * 商品规格
    */
    @Schema(description = "商品规格")
    private String saleAttr;
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
    @Schema(description = "操作者", hidden=true)
    private String userId;
    /**
    * 创建时间
    */
    @Schema(description = "创建时间", hidden=true)
    private LocalDateTime createTime;
    /**
    * 修改时间
    */
    @Schema(description = "修改时间", hidden=true)
    private LocalDateTime updateTime;

}