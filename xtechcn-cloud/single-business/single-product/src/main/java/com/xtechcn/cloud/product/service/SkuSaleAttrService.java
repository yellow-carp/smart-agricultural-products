package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.SkuSaleAttr;
import com.xtechcn.cloud.product.model.SaleAttrModel;
import com.xtechcn.cloud.product.model.po.ProductSkuMaParam;

import java.util.Collection;
import java.util.List;

/**
* 商品SKU销售属性值
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
public interface SkuSaleAttrService extends IService<SkuSaleAttr> {

    /**
     * SKU 销售属性值查询
     */
    List<SkuSaleAttr> listSkuSaleAttrs(String sku);

    /**
     * SPU 下的所有SKU销售属性值查询
     */
    List<SkuSaleAttr> listSkuSaleAttrsBySpu(String spu);

    /**
     * 创建或修改SKU销售属性信息
     *
     * @param productSkuParam 商品SKU 参数对象
     * @param saleAttrs       销售属性数据
     */
    void upsertSkuSaleAttrs(ProductSkuMaParam productSkuParam, List<SaleAttrModel> saleAttrs);

    /**
     * 通过SKU 编码批量删除
     */
    void removeBySkuIds(String spu, Collection<String> removeSkus);

    /**
     * 通过SKU 批量查询
     */
    List<SkuSaleAttr> listBySkus(List<String> skus);
}