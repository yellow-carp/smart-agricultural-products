package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.SpuSpecValue;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;

import java.util.List;

/**
* spu规格属性值
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
public interface SpuSpecValueService extends IService<SpuSpecValue> {

    /**
     * 移除规格名之后移除规格值
     */
    void removeByName(Long nameId);
    /**
     * 查询SPU 规格属性值
     */
    List<SpuSpecValue> listSpecAttrValuesWithCache(String spu);
    /**
     * 创建或修改SPU 规格属性
     */
    void upsertSpecValue(ProductSpuMaParam productSpuModel);
    /**
     * 删除spu 规格属性值
     */
    void removeBySpu(String spu);
}