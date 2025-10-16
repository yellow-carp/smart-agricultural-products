package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.ProductSku;
import com.xtechcn.cloud.product.entity.ProductSpu;
import com.xtechcn.cloud.product.model.po.ProductSkuMaParam;

import java.util.List;

/**
* 商品SKU
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
public interface ProductSkuService extends IService<ProductSku> {

    /**
     * 商品SKU数据转换
     */
    ProductSku convertEntity(ProductSkuMaParam spu);
    /**
     * 数据初始换处理
     */
    void upsertInit(ProductSpu productSpu, ProductSku productSku);
    /**
     * SKU查询商品
     */
    ProductSku findWithCache(String sku);
    /**
     * 查询SPU 下的SKU
     */
    List<ProductSku> listBySpu(String spu);

}