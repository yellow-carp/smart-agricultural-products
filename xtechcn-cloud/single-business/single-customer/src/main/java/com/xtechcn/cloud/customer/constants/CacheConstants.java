package com.xtechcn.cloud.customer.constants;

/**
 * 缓存常量
 *
 * @author hanjie
 * @since 2025-9-12 09:12:52
 */
public interface CacheConstants {

    /**
     * 车型缓存key
     */
    String CAR_CACHE_KEY = "ct:car:id";
    /**
     * 区域缓存key
     */
    String AREA_CACHE_KEY = "ct:area:id";
    /**
     * 包装形式缓存key
     */
    String PACKAGING_CACHE_KEY = "ct:packaging:id";
    /**
     * 自动收货配置缓存key
     */
    String RECEIVECONFIG_CACHE_KEY = "ct:receiveconfig:id";
    /**
     * 物流成本缓存key
     */
    String DELIVERYCOST_CACHE_KEY = "ct:deliverycost:id";

    /**
     * 采购商缓存key
     */
    String PURCHASER_CACHE_KEY = "ct:purchaser:userid";
}
