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
import java.time.LocalDateTime;

/**
* 售后详情表
*
* @author hanjie
* @since 2025-09-16 16:07:51
*/
@Getter
@Setter
@Schema(description = "售后详情表")
@TableName(value = "pd_after_sale_detail")
public class AfterSaleDetail extends Model<AfterSaleDetail>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 售后详情id
    */
    @TableId
    @Schema(description = "售后详情id", hidden=true)
    private String id;
    /**
    * 售后id
    */
    @Schema(description = "售后id")
    private String afterSaleId;
    /**
    * 关联的订单编号
    */
    @Schema(description = "关联的订单编号")
    private String orderNo;
    /**
    * 关联的订单详情编号
    */
    @Schema(description = "关联的订单详情编号")
    private String orderDetailNo;
    /**
    * 退款金额
    */
    @Schema(description = "退款金额")
    private MoneyPenny refundAmount;
    /**
    * 采购商id
    */
    @Schema(description = "采购商id", hidden=true)
    private String userId;
    /**
    * 创建人
    */
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人", hidden=true)
    private String createBy;
    /**
    * 逻辑删除
    */
    @JsonIgnore
    @TableLogic
    @TableField(select = false)
    @Schema(description = "逻辑删除", hidden=true)
    private Boolean isDelete;
    /**
    * 创建时间
    */
    @Schema(description = "创建时间", hidden=true)
    private LocalDateTime createTime;
    /**
    * 更新时间
    */
    @Schema(description = "更新时间", hidden=true)
    private LocalDateTime updateTime;

}