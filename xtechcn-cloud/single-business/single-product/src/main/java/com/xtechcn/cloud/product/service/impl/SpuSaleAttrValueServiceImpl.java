package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.constants.ProductResult;
import com.xtechcn.cloud.product.entity.SkuSaleAttr;
import com.xtechcn.cloud.product.entity.SpuSaleAttrValue;
import com.xtechcn.cloud.product.mapper.SkuSaleAttrMapper;
import com.xtechcn.cloud.product.mapper.SpuSaleAttrValueMapper;
import com.xtechcn.cloud.product.model.SaleAttrModel;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.service.SpuSaleAttrValueService;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.utils.ICollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
* 商品销售属性值表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Service
@RequiredArgsConstructor
public class SpuSaleAttrValueServiceImpl extends ServiceImpl<SpuSaleAttrValueMapper, SpuSaleAttrValue> implements SpuSaleAttrValueService {

    private final SkuSaleAttrMapper skuSaleAttrMapper;

    @Override
    @CacheEvict(value = CacheConstants.SALE_ATTR_SPU_CACHE, key = "#productSpuModel.getSpu()")
    public void upsertSaleAttrsValue(ProductSpuMaParam productSpuModel) {
        List<SaleAttrModel> saleAttrViewModels = productSpuModel.getSaleAttrs();
        String spu = productSpuModel.getSpu();
        // 参数验证,不可以属性值相同
        for (SaleAttrModel saleAttrViewModel : saleAttrViewModels) {
            List<SaleAttrModel.AttrValue> attrValues = saleAttrViewModel.getAttrValues();
            // 销售属性值去重处理
            Set<SaleAttrModel.AttrValue> attrValueSet = ICollUtil.process2Set(attrValues);
            // 销售属性值重复验证
            if (attrValues.size() != attrValueSet.size()) throw new ParamException(ProductResult.SALE_ATTR_REPEAT);
        }
        if (productSpuModel.isAdd()) {
            // 新增时清理客户端传参的属性ID（新增时不可能存在ID）
            saleAttrViewModels.forEach(attr -> attr.getAttrValues().forEach(value -> value.setValueId(null)));
            this.upsertSaleAttrsValue(spu, saleAttrViewModels);
            return;
        }

        // 修改的情况需要将原有的属性信息进行清理
        List<SpuSaleAttrValue> spuSaleAttrValues = this.listSpuSaleAttrValuesWithCache(spu);
        // 原数据中没有相应的销售属性信息,与新增一样的逻辑
        if (spuSaleAttrValues.isEmpty()) {
            saleAttrViewModels.forEach(attr -> attr.getAttrValues().forEach(value -> value.setValueId(null)));
            this.upsertSaleAttrsValue(spu, saleAttrViewModels);
            return;
        }

        // 数据库中已经存在数据,需要修改和移除无效数据
        Set<Long> existValueIds = ICollUtil.process2Set(spuSaleAttrValues, SpuSaleAttrValue::getId);
        for (SaleAttrModel saleAttrViewModel : saleAttrViewModels) {
            for (SaleAttrModel.AttrValue attrValue : saleAttrViewModel.getAttrValues()) {
                Long paramId = attrValue.getValueId();
                // 新增的属性,参数中没有ID,无需处理
                if (null == paramId) continue;
                // 数据库里已经存在的销售属性不需要处理
                if (existValueIds.contains(paramId)) {
                    // 需要移除的
                    existValueIds.remove(paramId);
                    continue;
                }
                // 参数中的ID 是数据库中不存在的,则为无效的Id,需要清理参数值
                attrValue.setValueId(null);
            }
        }
        // 如果库里的Id,比参数中有效ID 剩余的部分,则移除库里数据
        if (!existValueIds.isEmpty()) {
            this.removeByIds(existValueIds);
            // 移除商品SKU销售属性值
            this.removeSkuSaleAttr(existValueIds);
        }
        // 执行数据库更新
        this.upsertSaleAttrsValue(spu, saleAttrViewModels);
    }

    @Override
    @Cacheable(value = CacheConstants.SALE_ATTR_SPU_CACHE, key = "#spu", unless = "#result.isEmpty()")
    public List<SpuSaleAttrValue> listSpuSaleAttrValuesWithCache(String spu) {
        return this.list(Wrappers.<SpuSaleAttrValue>lambdaQuery().eq(SpuSaleAttrValue::getSpu, spu));
    }

    @Override
    @CacheEvict(value = CacheConstants.SALE_ATTR_SPU_CACHE, key = "#productSpuModel.getSpu()")
    public void removeSaleAttrsValue(ProductSpuMaParam productSpuModel) {
        String spu = productSpuModel.getSpu();
        List<SpuSaleAttrValue> spuSaleAttrValues = this.listSpuSaleAttrValuesWithCache(spu);
        for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValues) {
            this.removeWithCache(spuSaleAttrValue);
        }
        Set<Long> attrIds = ICollUtil.process2Set(spuSaleAttrValues, SpuSaleAttrValue::getId);
        // 移除商品SKU销售属性值
        if (!ICollUtil.isEmpty(attrIds)) this.removeSkuSaleAttr(attrIds);
    }

    @Override
    public void removeBySpu(String spu) {
        this.remove(Wrappers.<SpuSaleAttrValue>lambdaQuery().eq(SpuSaleAttrValue::getSpu, spu));
    }

    private void upsertSaleAttrsValue(String spu, Collection<SaleAttrModel> saleAttrViewModels) {
        for (SaleAttrModel saleAttrViewModel : saleAttrViewModels) {
            // 销售属性值
            List<SaleAttrModel.AttrValue> attrValues = saleAttrViewModel.getAttrValues();
            for (int i = 0; i < attrValues.size(); i++) {
                // 每一组销售属性值遍历
                SaleAttrModel.AttrValue attrValue = attrValues.get(i);
                Long valueId = attrValue.getValueId();
                // 创建SPU销售属性值数据
                SpuSaleAttrValue saleAttrValue = SpuSaleAttrValue.create(spu, saleAttrViewModel.getAttrId(), attrValue.getAttrValue())
                        .withId(valueId).image(attrValue.getImage()).sort(i);
                // 没有ID,新增的销售属性值
                boolean isOk = null == valueId ? this.save(saleAttrValue)
                        // 修改的是销售属性的值显示
                        : this.updateById(saleAttrValue);
                if (!isOk) throw new RollBackException();

                // 属性值Id数据回填
                if (null == valueId) attrValue.setValueId(saleAttrValue.getId());
            }
        }
    }

    private void removeSkuSaleAttr(Set<Long> existValueIds) {
        // 移除商品SKU销售属性值
        List<SkuSaleAttr> skuSaleAttrs = skuSaleAttrMapper.selectList(Wrappers.<SkuSaleAttr>lambdaQuery().in(SkuSaleAttr::getValueId, existValueIds));
        if (!skuSaleAttrs.isEmpty()) {
            skuSaleAttrMapper.deleteByIds(ICollUtil.process2List(skuSaleAttrs, SkuSaleAttr::getId));
        }
    }


    private boolean removeWithCache(SpuSaleAttrValue saleAttrValue) {
        return this.removeById(saleAttrValue.getId());
    }
}
