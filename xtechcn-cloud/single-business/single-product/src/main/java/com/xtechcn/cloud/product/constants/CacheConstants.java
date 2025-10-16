package com.xtechcn.cloud.product.constants;

/**
 * 缓存常量
 *
 * @author hanjie
 * @since 2025-9-12 09:12:52
 */
public interface CacheConstants {

    /**
     * 商品分类缓存
     */
    String CATEGORY_HASH_KEY = "pd:category";
    /**
     * 激活的分类树缓存 key
     */
    String CATEGORY_TREE_CACHE = "pd:category:tree";

    /**
     * 规格名分类缓存key
     */
    String SPEC_ATTR_NAME_CAT_CACHE = "pd:attr:spec:cat";
    /**
     * SPU 规格属性值缓存
     */
    String SPEC_ATTR_VALUE_SPU_CACHE = "pd:attr:spec:spu";
    /**
     * 分类销售属性缓存key
     */
    String SALE_ATTR_CAT_CACHE = "pd:attr:sale:cat";
    /**
     * SPU销售属性缓存key
     */
    String SALE_ATTR_SPU_CACHE = "pd:attr:sale:spu";
    /**
     * SPU 下所有SKU 的销售属性值
     */
    String SALE_ATTR_REL_SKU_CACHE = "pd:attr:sale:sku";

    /**
     * SPU 信息缓存
     */
    String PRODUCT_SPU_CACHE = "pd:info:spu";
    /**
     * SPU 扩展数据
     */
    String PRODUCT_SPU_EXTRA_CACHE = "pd:info:spu:extra";
    /**
     * SKU 信息缓存
     */
    String PRODUCT_SKU_CACHE = "pd:info:sku";
    String PRODUCT_SKU_EXTRA_CACHE = "pd:info:sku:extra";
    /**
     * 品牌数据缓存
     */
    String BRAND_ID_CACHE = "pd:brand";
    String BRAND_DEFAULT_CACHE = "pd:brand:default";
    /**
     * 商品每日上架调用次数
     */
    String PRODUCT_UP_COUNT_CACHE = "pd:up:count";
    String PRODUCT_UP_PERSONAL_COUNT_CACHE = "pd:up:personal:count";
}
