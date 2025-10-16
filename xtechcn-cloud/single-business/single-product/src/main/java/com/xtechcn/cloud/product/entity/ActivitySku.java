package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.io.Serial;
    import java.math.BigDecimal;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* 特价商品管理
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "特价商品管理")
@TableName(value = "pd_activity_sku")
public class ActivitySku extends Model<ActivitySku>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键id
    */
    @TableId
    @Schema(description = "主键id", hidden=true)
    private String id;
    /**
    * 商品sku
    */
    @Schema(description = "商品sku")
    private String sku;
    /**
    * 商品Spu
    */
    @Schema(description = "商品Spu")
    private String spu;
    /**
    * 供应商Id
    */
    @Schema(description = "供应商Id")
    private String supplierId;
    /**
    * 原价(分)
    */
    @Schema(description = "原价(分)")
    private MoneyPenny unitPrice;
    /**
    * 折扣方式：1-固定价，2-折扣率80%，3-折扣率70%，4-折扣率60%
    */
    @Schema(description = "折扣方式：1-固定价，2-折扣率80%，3-折扣率70%，4-折扣率60%")
    private Integer discountModel;
    /**
    * 特价(分)
    */
    @Schema(description = "特价(分)")
    private MoneyPenny specialPrice;
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
    * 活动日期
    */
    @Schema(description = "活动日期")
    private LocalDate actDate;
    /**
    * 状态：1-准备中，2-进行中，3-已结束
    */
    @Schema(description = "状态：1-准备中，2-进行中，3-已结束")
    private Integer state;
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