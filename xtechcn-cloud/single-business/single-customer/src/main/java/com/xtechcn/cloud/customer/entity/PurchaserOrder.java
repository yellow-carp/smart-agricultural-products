package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDateTime;

/**
 * 采购商入驻订单
 *
 * @author hanjie
 * @since 2025-09-18 16:58:25
 */
@Getter
@Setter
@Schema(description = "采购商入驻订单")
@TableName(value = "cu_purchaser_order")
public class PurchaserOrder extends Model<PurchaserOrder> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 订单号
     */
    @TableId
    @Schema(description = "订单号")
    private String orderNo;
    /**
     * 实际支付金额
     */
    @Schema(description = "实际支付金额")
    private MoneyPenny payAmount;
    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    private String title;
    /**
     * 支付时间
     */
    @Schema(description = "支付时间")
    private LocalDateTime payTime;
    /**
     * 订单状态（1：待支付，2：支付锁定，3：已取消，4：已支付）
     */
    @Schema(description = "订单状态（1：待支付，2：支付锁定，3：已取消，4：已支付）")
    private Integer orderState;
    /**
     * 支付方式
     */
    @Schema(description = "支付方式")
    private String channelKey;
    /**
     * 交易单号
     */
    @Schema(description = "交易单号")
    private String tradeNo;
    /**
     * 下单用户id
     */
    @Schema(description = "下单用户id", hidden = true)
    private String userId;
    /**
     * 下单人姓名
     */
    @Schema(description = "下单人姓名")
    private String createName;
    /**
     * 下单人手机号
     */
    @Schema(description = "下单人手机号")
    private String createNameTel;
    /**
     * 订单备注
     */
    @Schema(description = "订单备注")
    private String remark;
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
     * 修改时间
     */
    @Schema(description = "修改时间", hidden = true)
    private LocalDateTime updateTime;

    public PurchaserOrder newInstance() {
        PurchaserOrder purchaserOrder = new PurchaserOrder();
        purchaserOrder.orderNo = this.orderNo;
        return purchaserOrder;
    }

    public PurchaserOrder state(Integer state) {
        this.orderState = state;
        return this;
    }
}