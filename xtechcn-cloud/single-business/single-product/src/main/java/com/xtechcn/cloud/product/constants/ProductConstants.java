package com.xtechcn.cloud.product.constants;

import com.xtechcn.common.core.lang.IPercent;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 客户模块常量
 *
 * @author Alay
 * @since 2025-08-31 16:34
 */
public interface ProductConstants {

    /**
     * 上架动作
     */
    int DOWN_ACTION = 0;
    /**
     * 下架动作
     */
    int UP_ACTION = 1;
    /**
     * 商品分类最多三级
     */
    int LOW_CAT_LEVEL = 3;
    /**
     * 销售属性层级最高三层
     */
    int SALE_ATTR_MAX_LEVEL = 3;
    /**
     * 销售属性最多允许组合数量
     */
    int SALE_ATTR_MAX_SIZE = 30;
    /**
     * 自营商城编码标识
     */
    String SKU_PREFIX = "hnzdjwm";
    /**
     * 自营商城编码标识
     */
    String SPU_PREFIX = "hnzdj";
    /**
     * 商品编码自增 key
     */
    String SPU_NO_INCREMENT_KEY = "product:no:increment:spu";
    String SKU_NO_INCREMENT_KEY = "product:no:increment:sku";

    /**
     * 默认售后政策(7天无理由)
     */
    int DEF_AFTER_POLICY = 1;
    /**
     * 默认品牌ID
     */
    long DEF_BRAND_ID = 1L;

    /**
     * 属性值最大长度
     */
    int ATTR_VALUE_MAX_LENGTH = 511;
    /**
     * 规格属性最大支持搜索数量
     */
    int SPEC_VALUE_MAX_SEARCH = 3;
    /**
     * 包邮
     */
    int FREE_SHIPPING = 1;
    /**
     * 不包邮
     */
    int FREE_SHIPPING_NOT_AVAILABLE = 2;
    /**
     * 最低包邮购买数量
     */
    int FREE_MIN_NUM = 1;
    /**
     * 商品默认上架市场(自营、大单议价)
     */
    String DEFAULT_TO_MARKET = "010";
    /**
     * 默认税率
     */
    IPercent TAX_RATE = IPercent.valueOf("13%");
    /**
     * 进价与市场价转换率
     */
    BigDecimal MARKET_RATE = new BigDecimal("1.2");
    /**
     * 匹配正整数
     */
    Pattern PATTERN = Pattern.compile("(\\d+)[（(]?.*");
}
