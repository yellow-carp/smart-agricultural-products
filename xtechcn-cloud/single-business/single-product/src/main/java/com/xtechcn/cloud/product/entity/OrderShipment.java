package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
* 订单-发货关系表
*
* @author hanjie
* @since 2025-09-16 09:45:56
*/
@Getter
@Setter
@Schema(description = "订单-发货关系表")
@TableName(value = "pd_order_shipment")
public class OrderShipment extends Model<OrderShipment>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键id
    */
    @TableId
    @Schema(description = "主键id", hidden=true)
    private String id;
    /**
    * 发货单id
    */
    @Schema(description = "发货单id")
    private String taskId;
    /**
    * 订单编号
    */
    @Schema(description = "订单编号")
    private String orderNo;
    /**
    * 订单详情编号
    */
    @Schema(description = "订单详情编号")
    private String orderDetailNo;
    /**
    * 供应商id
    */
    @Schema(description = "供应商id")
    private String supplierId;
    /**
    * 发货状态（1：待发货，2：已发货）
    */
    @Schema(description = "发货状态（1：待发货，2：已发货）")
    private Integer state;
    /**
    * 邮寄时间
    */
    @Schema(description = "邮寄时间")
    private LocalDateTime postTime;
    /**
    * 采购商id
    */
    @Schema(description = "采购商id", hidden=true)
    private String userId;
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
    * 修改时间
    */
    @Schema(description = "修改时间", hidden=true)
    private LocalDateTime updateTime;

}