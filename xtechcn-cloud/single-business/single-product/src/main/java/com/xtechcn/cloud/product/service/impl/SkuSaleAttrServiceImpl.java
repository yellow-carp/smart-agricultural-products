package com.xtechcn.cloud.product.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.entity.SkuSaleAttr;
import com.xtechcn.cloud.product.mapper.SkuSaleAttrMapper;
import com.xtechcn.cloud.product.model.SaleAttrModel;
import com.xtechcn.cloud.product.model.po.ProductSkuMaParam;
import com.xtechcn.cloud.product.service.SkuSaleAttrService;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.core.utils.IStrPool;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 商品SKU销售属性值
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
@Service
public class SkuSaleAttrServiceImpl extends ServiceImpl<SkuSaleAttrMapper, SkuSaleAttr> implements SkuSaleAttrService {

    @Override
    public List<SkuSaleAttr> listSkuSaleAttrs(String sku) {
        return this.list(Wrappers.<SkuSaleAttr>lambdaQuery().eq(SkuSaleAttr::getSku, sku));
    }

    @Override
    @Cacheable(value = CacheConstants.SALE_ATTR_REL_SKU_CACHE, key = "#spu", unless = "#result.isEmpty()")
    public List<SkuSaleAttr> listSkuSaleAttrsBySpu(String spu) {
        return this.list(Wrappers.<SkuSaleAttr>lambdaQuery().eq(SkuSaleAttr::getSpu, spu));
    }

    @Override
    @CacheEvict(value = CacheConstants.SALE_ATTR_REL_SKU_CACHE, key = "#productSkuParam.getSpu()")
    public void upsertSkuSaleAttrs(ProductSkuMaParam productSkuParam, List<SaleAttrModel> saleAttrs) {
        // 没有配置销售属性的情况
        if (null == saleAttrs || saleAttrs.isEmpty()) return;
        // SKU 销售属性值组合
        String saleAttrsKey = productSkuParam.getSaleAttrs();
        // Sku 销售属性值数组
        String[] skuSaleAttrValueArr = StrUtil.splitToArray(saleAttrsKey, IStrPool.PIPE);

        // 当前sku 销售属性信息
        List<SkuSaleAttr> skuSaleAttrs = productSkuParam.isAdd() ? new ArrayList<>() : this.listSkuSaleAttrs(productSkuParam.getSku());
        Map<Long, SkuSaleAttr> existSaleValueMap = IMapUtil.coll2Map(skuSaleAttrs, SkuSaleAttr::getValueId);

        // 每一组销售属性名称
        for (int i = 0; i < saleAttrs.size(); i++) {
            SaleAttrModel spuSaleAttr = saleAttrs.get(i);
            // 每一组销售属性值
            List<SaleAttrModel.AttrValue> spuSaleAttrValues = spuSaleAttr.getAttrValues();

            // 销售属性值
            String skuSaleAttrValue = skuSaleAttrValueArr[i];
            for (SaleAttrModel.AttrValue saleAttrValue : spuSaleAttrValues) {
                // 匹配SKU当前销售属性值
                if (Objects.equals(skuSaleAttrValue, saleAttrValue.getAttrValue().trim())) {
                    // 创建新的SKU销售属性关联
                    SkuSaleAttr newSkuSaleAttr = SkuSaleAttr.create(productSkuParam.getSpu(), productSkuParam.getSku(), spuSaleAttr.getAttrId(), saleAttrValue.getValueId());

                    // SKU原有的销售属性值关系
                    if (skuSaleAttrs.isEmpty()) {
                        // sku 之前没有销售属性值数据（新增时或修改时添加了销售属性数据）
                        boolean isOk = this.save(newSkuSaleAttr);
                        if (!isOk) throw new RollBackException();
                        break;
                    }
                    // 弹出获取SKU 原来的销售属性关联
                    SkuSaleAttr existSkuSaleAttr = IMapUtil.popup(existSaleValueMap, saleAttrValue.getValueId());
                    if (existSkuSaleAttr == null) {
                        // 没有匹配到原有的销售属性关系
                        boolean isOk = this.save(newSkuSaleAttr);
                        if (!isOk) throw new RollBackException();
                        break;
                    }

                    // 新的属性与老的属性保持一致,无需更改
                    if (newSkuSaleAttr.equals(existSkuSaleAttr)) break;
                    // 变更
                    existSkuSaleAttr = existSkuSaleAttr.changeValue(newSkuSaleAttr);
                    boolean isOk = this.updateById(existSkuSaleAttr);
                    if (!isOk) throw new RollBackException();
                }
            }

        }
        // 移除多余的销售属性
        if (!existSaleValueMap.isEmpty()) {
            for (Map.Entry<Long, SkuSaleAttr> entry : existSaleValueMap.entrySet()) {
                this.removeById(entry.getKey());
            }
        }
    }

    @Override
    @CacheEvict(value = CacheConstants.SALE_ATTR_REL_SKU_CACHE, key = "#spu")
    public void removeBySkuIds(String spu, Collection<String> removeSkus) {
        this.remove(Wrappers.<SkuSaleAttr>lambdaQuery().in(SkuSaleAttr::getSku, removeSkus));
    }

    @Override
    public List<SkuSaleAttr> listBySkus(List<String> skus) {
        return this.list(Wrappers.<SkuSaleAttr>lambdaQuery().in(SkuSaleAttr::getSku, skus));
    }
}
