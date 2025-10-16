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
 * 订单结算详情
 *
 * @author hanjie
 * @since 2025-09-16 16:07:51
 */
@Getter
@Setter
@Schema(description = "订单结算详情")
@TableName(value = "pd_order_settle")
public class OrderSettle extends Model<OrderSettle> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 订单编号
     */
    @TableId
    @Schema(description = "订单编号")
    private String orderNo;
    /**
     * 供应商id
     */
    @Schema(description = "供应商id")
    private String supplierId;
    /**
     * 实际支付金额(分)
     */
    @Schema(description = "实际支付金额(分)")
    private MoneyPenny payPrice;
    /**
     * 退款金额
     */
    @Schema(description = "退款金额")
    private MoneyPenny refundAmount;
    /**
     * 结算金额
     */
    @Schema(description = "结算金额")
    private MoneyPenny settleAmount;
    /**
     * 平台抽成金额
     */
    @Schema(description = "平台抽成金额")
    private MoneyPenny platformAmount;
    /**
     * 供应商应结算金额
     */
    @Schema(description = "供应商应结算金额")
    private MoneyPenny supplierAmount;
    /**
     * 软件服务抽成
     */
    @Schema(description = "软件服务抽成")
    private MoneyPenny softwareAmount;
    /**
     * 结算状态：1-待结算，2-已结算
     */
    @Schema(description = "结算状态：1-待结算，2-已结算")
    private Integer settleState;
    /**
     * 结算时间
     */
    @Schema(description = "结算时间")
    private LocalDateTime settleTime;
    /**
     * 到账状态：1-未到账，2-已到账
     */
    @Schema(description = "到账状态：1-未到账，2-已到账")
    private Integer receiptState;
    /**
     * 到账时间
     */
    @Schema(description = "到账时间")
    private LocalDateTime receiptTime;
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
     * 更新时间
     */
    @Schema(description = "更新时间", hidden = true)
    private LocalDateTime updateTime;

}