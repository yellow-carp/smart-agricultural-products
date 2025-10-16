package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.constants.ProductResult;
import com.xtechcn.cloud.product.entity.SpuSaleAttr;
import com.xtechcn.cloud.product.mapper.SpuSaleAttrMapper;
import com.xtechcn.cloud.product.model.SaleAttrModel;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.service.SpuSaleAttrService;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.utils.ICollUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
* SPU销售属性
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr> implements SpuSaleAttrService {

    @Override
    @Cacheable(value = CacheConstants.SALE_ATTR_CAT_CACHE, key = "#spu", unless = "#result.isEmpty()")
    public List<SpuSaleAttr> listSpuSaleAttrsWithCache(String spu) {
        return this.list(Wrappers.<SpuSaleAttr>lambdaQuery().eq(SpuSaleAttr::getSpu, spu));
    }

    @Override
    @CacheEvict(value = CacheConstants.SALE_ATTR_CAT_CACHE, key = "#productSpuModel.getSpu()")
    public void upsertSaleAttrs(ProductSpuMaParam productSpuModel) {
        List<SaleAttrModel> saleAttrs = productSpuModel.getSaleAttrs();
        // 参数验证
        Set<String> names = ICollUtil.process2Set(saleAttrs, SaleAttrModel::getAttrName);
        // 没有填写销售属性名
        if (names.isEmpty()) throw new ParamException(ProductResult.SALE_ATTR_BLANK);
        // 销售属性名重复的
        if (names.size() != saleAttrs.size()) throw new ParamException(ProductResult.SALE_ATTR_REPEAT);

        String spu = productSpuModel.getSpu();
        // 销售属性新增处理
        if (productSpuModel.isAdd()) {
            // 新增时清理客户端传参的属性ID（新增时不可能存在ID）
            saleAttrs.forEach(attr -> attr.setAttrId(null));
            this.upsertSaleAttrs(spu, saleAttrs);
            return;
        }

        // 修改的情况需要将原有的属性信息进行清理
        List<SpuSaleAttr> spuSaleAttrs = this.listSpuSaleAttrsWithCache(spu);
        // 原数据中没有相应的销售属性信息,与新增一样的逻辑
        if (spuSaleAttrs.isEmpty()) {
            // 新增时清理客户端传参的属性ID（新增时不可能存在ID）
            saleAttrs.forEach(attr -> attr.setAttrId(null));
            this.upsertSaleAttrs(spu, saleAttrs);
            return;
        }

        // 数据库中已经存在数据,需要修改和移除无效数据
        Set<Long> existAttrIds = ICollUtil.process2Set(spuSaleAttrs, SpuSaleAttr::getId);
        for (SaleAttrModel saleAttr : saleAttrs) {
            Long paramId = saleAttr.getAttrId();
            // 新增的属性,参数中没有ID,无需处理
            if (null == paramId) continue;
            // 数据库里已经存在的销售属性不需要处理
            if (existAttrIds.contains(paramId)) {
                existAttrIds.remove(paramId);
                continue;
            }
            // 参数中的ID 是数据库中不存在的,则为无效的Id,需要清理参数值
            saleAttr.setAttrId(null);
        }
        // 如果库里的Id,比参数中有效ID 剩余的部分,则移除库里数据
        if (!existAttrIds.isEmpty()) {
            this.removeByIds(existAttrIds);
        }
        // 执行数据库更新
        this.upsertSaleAttrs(spu, saleAttrs);
    }

    @Override
    @CacheEvict(value = CacheConstants.SALE_ATTR_CAT_CACHE, key = "#productSpuModel.getSpu()")
    public void removeSaleAttrs(ProductSpuMaParam productSpuModel) {
        String spu = productSpuModel.getSpu();
        List<SpuSaleAttr> spuSaleAttrs = this.listSpuSaleAttrsWithCache(spu);
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrs) {
            this.removeWithCache(spuSaleAttr);
        }
    }

    @Override
    public void removeBySpu(String spu) {
        this.remove(Wrappers.<SpuSaleAttr>lambdaQuery().eq(SpuSaleAttr::getSpu, spu));
    }
    private void upsertSaleAttrs(String spu, List<SaleAttrModel> saleAttrs) {
        for (int i = 0; i < saleAttrs.size(); i++) {
            SaleAttrModel saleAttr = saleAttrs.get(i);
            Long attrId = saleAttr.getAttrId();
            SpuSaleAttr spuSaleAttr = SpuSaleAttr.create(spu, saleAttr.getAttrName(), i).id(attrId);
            // 销售属性值ID 已经存在,说明已经创建了销售属性

            boolean isOk = null == attrId ? this.save(spuSaleAttr) : this.updateById(spuSaleAttr);
            // 失败回滚异常
            if (!isOk) throw new RollBackException();
            // 属性ID 回显填充数据
            saleAttr.setAttrId(spuSaleAttr.getId());
        }
    }
    private boolean removeWithCache(SpuSaleAttr spuSaleAttr) {
        return this.removeById(spuSaleAttr.getId());
    }
}
