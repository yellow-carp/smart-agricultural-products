package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.SpuSaleAttr;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;

import java.util.List;

/**
 * SPU销售属性
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    /**
     * SPU 销售属性
     */
    List<SpuSaleAttr> listSpuSaleAttrsWithCache(String spu);

    /**
     * 创建或更新SPU 销售属性
     */
    void upsertSaleAttrs(ProductSpuMaParam productSpuModel);

    /**
     * 删除销售属性
     */
    void removeSaleAttrs(ProductSpuMaParam productSpuModel);

    /**
     * 删除spu 销售属性
     */
    void removeBySpu(String spu);
}