package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.SpuSaleAttrValue;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;

import java.util.List;

/**
 * 商品销售属性值表
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
public interface SpuSaleAttrValueService extends IService<SpuSaleAttrValue> {
    /**
     * 创建或修改SPU销售属性
     */
    void upsertSaleAttrsValue(ProductSpuMaParam productSpuModel);

    /**
     * 查询SPU 的销售属性值
     */
    List<SpuSaleAttrValue> listSpuSaleAttrValuesWithCache(String spu);

    /**
     * 删除销售属性值
     */
    void removeSaleAttrsValue(ProductSpuMaParam productSpuModel);

    /**
     * 删除spu 销售属性值
     */
    void removeBySpu(String spu);
}