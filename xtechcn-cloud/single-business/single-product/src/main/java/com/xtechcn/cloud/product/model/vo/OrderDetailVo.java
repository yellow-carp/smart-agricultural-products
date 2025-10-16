package com.xtechcn.cloud.product.model.vo;

import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Weijixiao
 * @since 2025-09-17 16:09
 */
@Getter
@Setter
@Schema(description = "订单详情返回值")
public class OrderDetailVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 id
     */
    @Schema(description = "主键 id", hidden = true)
    private String id;
    /**
     * 订单号
     */
    @Schema(description = "订单号")
    private String orderNo;
    /**
     * sku
     */
    @Schema(description = "sku")
    private String sku;
    /**
     * 电建平台商品唯一标识
     */
    @Schema(description = "电建平台商品唯一标识")
    private String djSku;
    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    private String goodsName;
    /**
     * 条形码
     */
    @Schema(description = "条形码")
    private String goodsCode;
    /**
     * 商品品牌
     */
    @Schema(description = "商品品牌")
    private String brandName;
    /**
     * 规格名称
     */
    @Schema(description = "规格名称")
    private String specsData;
    /**
     * 商品单价
     */
    @Schema(description = "商品单价")
    private MoneyPenny price;
    /**
     * 商品总价
     */
    @Schema(description = "商品总价")
    private MoneyPenny totalPrice;
    /**
     * 净价
     */
    @Schema(description = "净价")
    private MoneyPenny netPrice;
    /**
     * 税额
     */
    @Schema(description = "税额")
    private MoneyPenny taxPrice;
    /**
     * 税率
     */
    @Schema(description = "税率")
    private BigDecimal taxRate;
    /**
     * 市场 / 官网参考价格
     */
    @Schema(description = "市场 / 官网参考价格")
    private MoneyPenny marketPrice;
    /**
     * 商品详情
     */
    @Schema(description = "商品详情")
    private String goodsDesc;
    /**
     * 商品图片
     */
    @Schema(description = "商品图片")
    private String goodsImage;
    /**
     * 采购数量
     */
    @Schema(description = "采购数量")
    private BigDecimal num;
}