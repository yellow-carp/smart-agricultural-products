package com.xtechcn.cloud.payment.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xtechcn.cloud.payment.constants.TradeStateEnum;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import com.xtechcn.common.serialization.lang.JsonObj;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Optional;


/**
 * 交易单表
 *
 * @author Alay
 * @since 2022-05-16 10:17:37
 */
@Getter
@Setter
@Schema(description = "交易信息表")
@TableName(value = "pay_trade", autoResultMap = true)
public class Trade extends Model<Trade> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 交易单号
     */
    @TableId(type = IdType.INPUT)
    @Schema(description = "交易单号")
    private String tradeNo;
    /**
     * 批次号
     */
    @Schema(description = "批次号")
    private String batchNo;
    /**
     * 支付渠道唯一标识
     */
    @Schema(description = "支付渠道唯一标识")
    private String channelKey;
    /**
     * 支付渠道
     */
    @Schema(description = "支付渠道")
    private String channel;
    /**
     * 付款方Id(可能是用户Id,也可能是供应商Id)
     */
    private String payerId;
    /**
     * 金额(分)
     */
    @Schema(description = "金额(分)")
    private MoneyPenny amount;
    /**
     * 支付金额(分)
     */
    @Schema(description = "支付金额(分)")
    private MoneyPenny payAmount;
    /**
     * 退款金额(分)
     */
    @Schema(description = "退款金额(分)")
    private MoneyPenny refundAmount;
    /**
     * 交易类型
     */
    @Schema(description = "交易类型")
    private Integer tradeType;
    /**
     * 币种
     */
    @Schema(description = "币种")
    private String currency = "CNY";
    /**
     * 支付状态
     */
    @Schema(description = "支付状态")
    private TradeStateEnum state;
    /**
     * 商品标题
     */
    @Schema(description = "商品标题")
    private String subject;
    /**
     * 商品描述信息
     */
    @Schema(description = "商品描述信息")
    private String body;
    /**
     * 附加额外信息
     */
    @Schema(description = "附加额外信息")
    private JsonObj extra;
    /**
     * 返回结果码
     */
    @Schema(description = "返回结果码")
    private String returnCode;
    /**
     * 支付成功内部通知地址
     */
    @Schema(description = "支付成功内部通知地址")
    private String notifyUrl;
    /**
     * 消息
     */
    @Schema(description = "消息")
    private String message;
    /**
     * 订单支付成功时间
     */
    @Schema(description = "订单支付成功时间")
    private Long successTime;
    /**
     * 客户端IP
     */
    @Schema(description = "客户端IP")
    private String clientIp;
    /**
     * 设备
     */
    @Schema(description = "设备")
    private String device;
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


    public Trade withNo() {
        Trade trade = new Trade();
        trade.tradeNo = this.tradeNo;
        return trade;
    }

    public Trade state(TradeStateEnum state) {
        this.state = state;
        return this;
    }


    public Trade successTime(LocalDateTime time) {
        time = Optional.ofNullable(time).orElse(LocalDateTime.now());
        this.successTime = LocalDateTimeUtil.toEpochMilli(time);
        return this;
    }

}
