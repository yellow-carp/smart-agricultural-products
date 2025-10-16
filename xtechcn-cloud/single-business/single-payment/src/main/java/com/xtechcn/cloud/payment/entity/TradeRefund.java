package com.xtechcn.cloud.payment.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xtechcn.cloud.payment.constants.RefundStateEnum;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Optional;


/**
 * 退款单表
 *
 * @author Alay
 * @since 2023-5-24 10:54:41
 */
@Getter
@Setter
@TableName("pay_trade_refund")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "交易退款表")
public class TradeRefund extends Model<TradeRefund> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 退款单号
     */
    @TableId(type = IdType.INPUT)
    @Schema(description = "退款单号")
    private String refundNo;
    /**
     * 外部退款编号(调用方的唯一标识),业务编码
     */
    @Schema(description = "外部退款编号")
    private String outRefundNo;
    /**
     * 原交易单号
     */
    @Schema(description = "原交易单号")
    private String tradeNo;
    /**
     * 流水系列号(微信支付订单号)
     */
    @Schema(description = "流水系列号")
    private String serialNo;
    /**
     * 退款原因说明
     */
    @Schema(description = "退款原因说明")
    private String reason;
    /**
     * 通知地址
     */
    @Schema(description = "通知地址")
    private String notifyUrl;
    /**
     * 币种
     */
    @Schema(description = "币种")
    private String currency = "CNY";
    /**
     * 付款方
     */
    @Schema(description = "付款方")
    private String payerId;
    /**
     * 原订单金额(分)
     */
    @Schema(description = "原订单金额(分)")
    private MoneyPenny totalAmount;
    /**
     * 退款金额(分)
     */
    @Schema(description = "退款金额(分)")
    private MoneyPenny refundAmount;
    /**
     * 额外参数
     */
    @Schema(description = "额外参数")
    private String extra;
    /**
     * 消息
     */
    @Schema(description = "消息")
    private String message;
    /**
     * 退款来源
     */
    @Schema(description = "退款来源")
    private String fromBy;
    /**
     * 支付状态
     */
    @Schema(description = "退款状态")
    private RefundStateEnum state;
    /**
     * 支付渠道唯一标识
     */
    @Schema(description = "支付渠道唯一标识")
    private String channelKey;
    /**
     * 订单支付成功时间
     */
    @Schema(description = "成功时间")
    private Long successTime;
    /**
     * 退款三方回调请求
     */
    @Schema(description = "退款三方回调请求")
    private String request;
    /**
     * 退款三方回调响应
     */
    @Schema(description = "退款三方回调响应")
    private String response;
    /**
     * 回调时间
     */
    @Schema(description = "回调时间")
    private String notifyTime;
    /**
     * 创建者
     */
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者", hidden = true)
    private String createBy;
    /**
     * 逻辑删除
     */
    @JsonIgnore
    @TableLogic
    @TableField(select = false)
    @Schema(description = "逻辑删除", hidden = true)
    private Boolean isDelete;
    /**
     * 修改时间
     */
    @Schema(description = "修改时间", hidden = true)
    private LocalDateTime updateTime;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间", hidden = true)
    private LocalDateTime createTime;

    public static TradeRefund withRefundNo(String refundNo) {
        TradeRefund tradeRefund = new TradeRefund();
        tradeRefund.refundNo = refundNo;
        return tradeRefund;
    }

    public TradeRefund successTime(LocalDateTime time) {
        time = Optional.ofNullable(time).orElse(LocalDateTime.now());
        this.successTime = LocalDateTimeUtil.toEpochMilli(time);
        return this;
    }

    public String tradeNo() {
        return this.tradeNo;
    }

    public TradeRefund request(String request) {
        this.request = request;
        return this;
    }

    public TradeRefund response(String response) {
        this.response = response;
        return this;
    }

    public TradeRefund notifyTime(String notifyTime) {
        this.notifyTime = null != notifyTime ? notifyTime : String.valueOf(System.currentTimeMillis());
        return this;
    }

}
