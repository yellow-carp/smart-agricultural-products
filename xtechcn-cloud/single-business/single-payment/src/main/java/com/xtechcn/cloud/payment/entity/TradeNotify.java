package com.xtechcn.cloud.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;


/**
 * 支付交易回调信息表
 *
 * @author Alay
 * @since 2022-05-16 10:17:37
 */
@Getter
@Setter
@TableName("pay_trade_notify")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付订单扩展表")
public class TradeNotify extends Model<TradeNotify> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 交易单号
     */
    @TableId(type = IdType.INPUT)
    @Schema(description = "交易单号")
    private String tradeNo;
    /**
     * 渠道订单号
     */
    @Schema(description = "渠道订单号")
    private String channelTradeNo;
    /**
     * 支付三方回调请求
     */
    @Schema(description = "支付三方回调请求")
    private String request;
    /**
     * 支付三方回调响应
     */
    @Schema(description = "支付三方回调响应")
    private String response;
    /**
     * 回调时间
     */
    @Schema(description = "回调时间")
    private String notifyTime;
    /**
     * 订单失效时间
     */
    @Schema(description = "订单失效时间")
    private String expireTime;

    public static TradeNotify builder(@NotBlank String tradeNo, @NotBlank String channelTradeNo) {
        return new TradeNotify().tradeNo(tradeNo).channelTradeNo(channelTradeNo);
    }

    private TradeNotify tradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }

    private TradeNotify channelTradeNo(String channelTradeNo) {
        this.channelTradeNo = channelTradeNo;
        return this;
    }

    public TradeNotify request(String request) {
        this.request = request;
        return this;
    }

    public TradeNotify response(String response) {
        this.response = response;
        return this;
    }

    public TradeNotify notifyTime(String notifyTime) {
        this.notifyTime = null != notifyTime ? notifyTime : String.valueOf(System.currentTimeMillis());
        return this;
    }

    public TradeNotify expireTime(String expireTime) {
        this.expireTime = expireTime;
        return this;
    }

}
