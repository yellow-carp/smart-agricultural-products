package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.mybatis.typehandler.JsonTypeHandler;
import com.xtechcn.common.mybatis.typehandler.Str2ListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* 订单表
*
* @author hanjie
* @since 2025-09-16 16:07:51
*/
@Getter
@Setter
@Schema(description = "订单表")
@TableName(value = "pd_order_info", autoResultMap = true)
public class OrderInfo extends Model<OrderInfo>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 订单编号
    */
    @TableId
    @Schema(description = "订单编号")
    private String orderNo;
    /**
    * 父订单编号
    */
    @Schema(description = "父订单编号")
    private String parentOrderNo;
    /**
     * 批次号
     */
    @Schema(description = "批次号")
    private String batchNo;
    /**
    * 收货地址id
    */
    @Schema(description = "收货地址id")
    private String addressId;
    /**
    * 收货人姓名
    */
    @Schema(description = "收货人姓名")
    private String contact;
    /**
    * 收货人电话
    */
    @Schema(description = "收货人电话")
    private String contactTel;
    /**
    * 行政区域全名
    */
    @Schema(description = "行政区域全名")
    private String regionFullName;
    /**
    * 详细地址
    */
    @Schema(description = "详细地址")
    private String detail;
    /**
    * 交易单号
    */
    @Schema(description = "交易单号")
    private String tradeNo;
    /**
    * 订单状态（1：待支付，2：支付锁定，3：已取消，4：已支付，7：待调整，11：待发货，12：已发货，14：已签收）
    */
    @Schema(description = "订单状态（1：待支付，2：支付锁定，3：已取消，4：已支付，7：待调整，11：待发货，12：已发货，14：已签收）")
    private Integer orderState;
    /**
    * 是否售后状态
    */
    @Schema(description = "是否售后状态")
    private Boolean isAfterSale;
    /**
    * 订单类型(1：正常订单，2-补货订单)
    */
    @Schema(description = "订单类型(1：正常订单，2-补货订单)")
    private Integer orderType;
    /**
    * 支付方式
    */
    @Schema(description = "支付方式")
    private String channelKey;
    /**
    * 支付时间
    */
    @Schema(description = "支付时间")
    private LocalDateTime payDate;
    /**
    * 运费总价
    */
    @Schema(description = "运费总价")
    private MoneyPenny freightFee;
    /**
    * 订单总金额
    */
    @Schema(description = "订单总金额")
    private MoneyPenny orderPrice;
    /**
    * 差额退款金额
    */
    @Schema(description = "差额退款金额")
    private MoneyPenny changeRfdPrice;
    /**
    * 差额退款时间
    */
    @Schema(description = "差额退款时间")
    private LocalDateTime changeFrdTime;
    /**
    * 实际支付金额
    */
    @Schema(description = "实际支付金额")
    private MoneyPenny payPrice;
    /**
    * 优惠金额
    */
    @Schema(description = "优惠金额")
    private MoneyPenny saveAmount;
    /**
    * 税额
    */
    @Schema(description = "税额")
    private MoneyPenny taxPrice;
    /**
    * 产区id
    */
    @Schema(description = "产区id")
    private String areaOriginId;
    /**
    * 销区id
    */
    @Schema(description = "销区id")
    private String areaSalesId;
    /**
    * 物流模式：1-整车，2-拼车
    */
    @Schema(description = "物流模式：1-整车，2-拼车")
    private Integer deliveryModel;
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
     * 付款类型
     */
    @Schema(description = "付款类型")
    private Integer payType;
    /**
    * 线下支付凭证
    */
    @Schema(description = "线下支付凭证")
    @TableField(typeHandler = Str2ListTypeHandler.class)
    private List<String> voucher;
    /**
    * 采购商id
    */
    @Schema(description = "采购商id", hidden=true)
    private String userId;
    /**
    * 供应商id
    */
    @Schema(description = "供应商id")
    private String supplierId;
    /**
    * 车型id
    */
    @Schema(description = "车型id")
    private String carId;
    /**
    * 是否评价
    */
    @Schema(description = "是否评价")
    private Boolean isEvaluate;
    /**
    * B端逻辑删除
    */
    @Schema(description = "B端逻辑删除")
    private Boolean isDeleteB;
    /**
    * C端逻辑删除
    */
    @Schema(description = "C端逻辑删除")
    private Boolean isDeleteC;
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