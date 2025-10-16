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
 * 售后表
 *
 * @author hanjie
 * @since 2025-09-16 16:07:51
 */
@Getter
@Setter
@Schema(description = "售后表")
@TableName(value = "pd_after_sale")
public class AfterSale extends Model<AfterSale> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识
     */
    @TableId
    @Schema(description = "唯一标识", hidden = true)
    private String id;
    /**
     * 关联的订单编号
     */
    @Schema(description = "关联的订单编号")
    private String orderNo;
    /**
     * 供应商id
     */
    @Schema(description = "供应商id")
    private String supplierId;
    /**
     * 售后状态（1：待处理、2：已取消、3：处理中、4：已完成、5：已拒绝、 6：退款失败）
     */
    @Schema(description = "售后状态（1：待处理、2：已取消、3：处理中、4：已完成、5：已拒绝、 6：退款失败）")
    private Integer state;
    /**
     * 售后类型（1:仅退款、2:退货退款，3:维修，4:换货）
     */
    @Schema(description = "售后类型（1:仅退款、2:退货退款，3:维修，4:换货）")
    private Integer type;
    /**
     * 退款原因（1：商品损坏，2：商品品质问题，3：与商家协商一致退款，4：其他）
     */
    @Schema(description = "退款原因（1：商品损坏，2：商品品质问题，3：与商家协商一致退款，4：其他）")
    private Integer reasonType;
    /**
     * 申请原因
     */
    @Schema(description = "申请原因")
    private String reason;
    /**
     * 凭证
     */
    @Schema(description = "凭证")
    private String voucher;
    /**
     * 退款金额
     */
    @Schema(description = "退款金额")
    private MoneyPenny refundAmount;
    /**
     * 审批意见
     */
    @Schema(description = "审批意见")
    private String auditMsg;
    /**
     * 采购商id
     */
    @Schema(description = "采购商id", hidden = true)
    private String userId;
    /**
     * 创建人
     */
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人", hidden = true)
    private String createBy;
    /**
     * 更新人
     */
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "更新人", hidden = true)
    private String upsertBy;
    /**
     * 逻辑删除
     */
    @JsonIgnore
    @TableLogic
    @TableField(select = false)
    @Schema(description = "逻辑删除", hidden = true)
    private Boolean isDelete;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间", hidden = true)
    private LocalDateTime createTime;
    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;
    /**
     * 更新时间
     */
    @Schema(description = "更新时间", hidden = true)
    private LocalDateTime updateTime;

}