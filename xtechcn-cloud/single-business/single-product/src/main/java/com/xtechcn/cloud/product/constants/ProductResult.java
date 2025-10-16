package com.xtechcn.cloud.product.constants;

import com.xtechcn.common.core.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 *
 * @author Alay
 * @since 2025-08-31 16:32
 */
@Getter
@AllArgsConstructor
public enum ProductResult implements BaseResult {

    CAT_HAS_CHILD("PD0100", "分类下具有子节点数据"),
    CAT_HAS_PRODUCT("PD0101", "三级分类下存在属性数据"),
    CAT_LEVEL_ILLEGAL("PE0102", "分类级别不符合"),
    CAT_TREE_INCOMPLETE("PD0103", "请补充分类下的属性数据"),
    CAT_ERROR("PD0104", "分类数据错误"),
    CAT_ATTR_NOT_EMPTY("PD0105", "分类下的属性不能空"),
    CAT_NOT_EXIST("PD0106", "三级分类不存在"),
    CAT_IS_EMPTY("PD0107", "分类不能为空"),
    SPEC_NAME_KEEP_LEAST_ONE("PD0108", "分类下至少保留一个属性"),


    BRAND_HAS_PRODUCT("PE0200", "品牌下有商品数据"),

    ATTR_UN_EXIST("PE0300", "属性信息不存在"),
    SPEC_GROUP_UN_EXIST("PE0301", "规格组不存在"),
    ATTR_HAS_MOUNTED("PE0302", "属性下存在数据挂载"),
    SPEC_GROUP_EMPTY("PE0303", "规格组ID不能空"),
    SALE_ATTR_HAS_SKU("PE0304", "销售属性下已存在SKU"),
    SALE_ATTR_BLANK("PE0305", "销售属性为空"),
    SALE_ATTR_REPEAT("PE0306", "销售属性重复"),
    SALE_ATTR_HAVE_SKU("PE0307", "销售属性下已经存在SKU"),
    SALE_ATTR_NUM_UN_MATCH("PE0308", "SKU与SPU销售属性数量不对等"),
    SALE_ATTR_LEVEL_TOO_HIGH("PE0309", "销售属性允许最大三层"),
    SALE_ATTR_SKU_TOO_MUCH("PE0310", "销售属性下SKU组合超过30个了"),
    SKU_SALE_ATTR_VALUE_INVALID("PE0311", "SKU销售属性值无效"),
    ATTR_NOT_ALLOW_CONTEXT("PE0312", "属性信息存在不允许的内容"),


    PRODUCT_STATE_ILLEGAL("PE0400", "产品状态不符合"),
    PRODUCT_UP_STATE("PE0401", "商品状态已上架"),
    PRODUCT_DOWN_STATE("PE0402", "商品已下架"),
    PRODUCT_IS_FULL("PE0410", "商品SPU下已经存满了"),
    PRODUCT_UN_EXIST("PE0411", "商品数据不存在"),
    PRODUCT_UNSUPPORTED_DEL("PE0420", "该商品不支持删除"),
    PRODUCT_PARAM_INVALID("PE0430", "商品参数无效"),

    ;
    private final String code;
    private final String message;
}
