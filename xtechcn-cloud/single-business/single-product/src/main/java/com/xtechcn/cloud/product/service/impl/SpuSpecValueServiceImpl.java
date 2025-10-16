package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.entity.SpuSpecValue;
import com.xtechcn.cloud.product.mapper.SpuSpecValueMapper;
import com.xtechcn.cloud.product.model.SpecAttrModel;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.service.SpuSpecValueService;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.utils.IStrUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * spu规格属性值
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
@Service
public class SpuSpecValueServiceImpl extends ServiceImpl<SpuSpecValueMapper, SpuSpecValue> implements SpuSpecValueService {

    @Override
    public void removeByName(Long nameId) {
        this.remove(Wrappers.<SpuSpecValue>lambdaQuery().eq(SpuSpecValue::getNameId, nameId));
    }

    @Override
    @Cacheable(value = CacheConstants.SPEC_ATTR_VALUE_SPU_CACHE, key = "#spu", unless = "#result.isEmpty()")
    public List<SpuSpecValue> listSpecAttrValuesWithCache(String spu) {
        return this.list(Wrappers.<SpuSpecValue>lambdaQuery().eq(SpuSpecValue::getSpu, spu));
    }

    @Override
    @CacheEvict(value = CacheConstants.SPEC_ATTR_VALUE_SPU_CACHE, key = "#productSpuModel.getSpu()")
    public void upsertSpecValue(ProductSpuMaParam productSpuModel) {
        String spu = productSpuModel.getSpu();
        for (SpecAttrModel specAttr : productSpuModel.getSpecAttrs()) {
            // 规格属性值为空,则不处理
            if (IStrUtil.isBlank(specAttr.getSpecValue())) {
                if (!productSpuModel.isAdd()) {
                    // 删除spu 的规格属性信息（存在则删除）
                    this.removeSpuSpecValues(spu, specAttr.getNameId());
                }
                continue;
            }
            Long valueId = specAttr.getValueId();
            // 创建SPU 规格信息
            SpuSpecValue spuSpecValue = SpuSpecValue.ofSpu(spu).andName(specAttr.getNameId(), specAttr.getSpecValue())
                    .withId(valueId).inSearch(false);
            // 属性值Id不存在则创建,存在则为更新值
            boolean isOk = valueId == null ? this.save(spuSpecValue) : this.updateById(spuSpecValue);
            // 回滚异常
            if (!isOk) throw new RollBackException();
            // 回填主键Id 数据
            if (valueId == null) {
                specAttr.setValueId(spuSpecValue.getId());
            }
        }
    }

    @Override
    public void removeBySpu(String spu) {

    }

    private void removeSpuSpecValues(String spu, Long nameId) {
        this.remove(Wrappers.<SpuSpecValue>lambdaQuery().eq(SpuSpecValue::getSpu, spu).eq(SpuSpecValue::getNameId, nameId));
    }
}
