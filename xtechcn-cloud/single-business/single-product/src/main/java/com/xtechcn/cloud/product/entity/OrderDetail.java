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
* 订单详情
*
* @author hanjie
* @since 2025-09-16 16:07:51
*/
@Getter
@Setter
@Schema(description = "订单详情")
@TableName(value = "pd_order_detail")
public class OrderDetail extends Model<OrderDetail>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 订单详情编号
    */
    @TableId
    @Schema(description = "订单详情编号", hidden=true)
    private String id;
    /**
    * 订单编号
    */
    @Schema(description = "订单编号")
    private String orderNo;
    /**
    * 供应商id
    */
    @Schema(description = "供应商id")
    private String supplierId;
    /**
    * 商品spu
    */
    @Schema(description = "商品spu")
    private String spu;
    /**
    * 商品sku
    */
    @Schema(description = "商品sku")
    private String sku;
    /**
    * 商品名称
    */
    @Schema(description = "商品名称")
    private String title;
    /**
    * 三级分类标识
    */
    @Schema(description = "三级分类标识")
    private Long cat3Id;
    /**
    * 分类全路径
    */
    @Schema(description = "分类全路径")
    private String fullCatName;
    /**
    * 总价
    */
    @Schema(description = "总价")
    private String poster;
    /**
    * 原订单数量
    */
    @Schema(description = "原订单数量")
    private Integer orderNum;
    /**
    * 实际装车数量
    */
    @Schema(description = "实际装车数量")
    private Integer num;
    /**
    * 单位id
    */
    @Schema(description = "单位id")
    private Long unitId;
    /**
    * 单位名称
    */
    @Schema(description = "单位名称")
    private String unitName;
    /**
    * 每单位抽成金额
    */
    @Schema(description = "每单位抽成金额")
    private MoneyPenny unitAmount;
    /**
    * 销售属性
    */
    @Schema(description = "销售属性")
    private String saleAttr;
    /**
    * 订单状态（1：待支付，2：支付锁定，3：已取消，4：已支付，7：待调整，11：待发货，12：已发货，14：已签收）
    */
    @Schema(description = "订单状态（1：待支付，2：支付锁定，3：已取消，4：已支付，7：待调整，11：待发货，12：已发货，14：已签收）")
    private Integer orderState;
    /**
    * 售后状态(31：申请退款，32：退款中，33：退款完成，34：拒绝退款，35：退款关闭，41：申请退货退款，42：退货退款中，43：退货退款完成，44：拒绝退货退款，45：退货退款关闭，51：申请换货，52：换货中，53：换货完成，54：拒绝换货，55：换货关闭，61：申请维修，62：维修中，63：维修完成，64：拒绝维修，65：维修关闭)
    */
    @Schema(description = "售后状态(31：申请退款，32：退款中，33：退款完成，34：拒绝退款，35：退款关闭，41：申请退货退款，42：退货退款中，43：退货退款完成，44：拒绝退货退款，45：退货退款关闭，51：申请换货，52：换货中，53：换货完成，54：拒绝换货，55：换货关闭，61：申请维修，62：维修中，63：维修完成，64：拒绝维修，65：维修关闭)")
    private Integer afterSaleState;
    /**
    * 含税单价(分)
    */
    @Schema(description = "含税单价(分)")
    private MoneyPenny unitPrice;
    /**
    * 税率(无小数点)
    */
    @Schema(description = "税率(无小数点)")
    private String taxRate;
    /**
    * 税额(分)
    */
    @Schema(description = "税额(分)")
    private MoneyPenny taxPrice;
    /**
    * 含税总价(分)
    */
    @Schema(description = "含税总价(分)")
    private MoneyPenny totalPrice;
    /**
    * 实际支付金额(分)
    */
    @Schema(description = "实际支付金额(分)")
    private MoneyPenny payPrice;
    /**
    * 优惠金额(分)
    */
    @Schema(description = "优惠金额(分)")
    private MoneyPenny saveAmount;
    /**
    * 采购商id
    */
    @Schema(description = "采购商id", hidden=true)
    private String userId;
    /**
    * 收货时间
    */
    @Schema(description = "收货时间")
    private LocalDateTime receiptTime;
    /**
    * 退款金额
    */
    @Schema(description = "退款金额")
    private MoneyPenny refundAmount;
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