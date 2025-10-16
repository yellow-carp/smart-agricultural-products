package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.entity.SpecName;
import com.xtechcn.cloud.product.mapper.SpecNameMapper;
import com.xtechcn.cloud.product.model.SpecAttrModel;
import com.xtechcn.cloud.product.service.SpecNameService;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.utils.ICollUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* spu规格名称
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Service
public class SpecNameServiceImpl extends ServiceImpl<SpecNameMapper, SpecName> implements SpecNameService {

    @Override
    public boolean cat3IdHasSpecName(String cat3Id) {
        return 0 != this.count(Wrappers.<SpecName>lambdaQuery().eq(SpecName::getCat3Id, cat3Id));
    }

    @Override
    @Cacheable(value = CacheConstants.SPEC_ATTR_NAME_CAT_CACHE, key = "#cat3Id", unless = "#result.isEmpty()")
    public List<SpecName> listByCat3IdWithCache(String cat3Id) {
        return this.list(Wrappers.<SpecName>lambdaQuery().eq(SpecName::getCat3Id, cat3Id));
    }

    @Override
    public List<SpecAttrModel> convertSpecAttrModels(List<SpecName> specNames) {
        List<SpecAttrModel> attrModels = new ArrayList<>();
        if (ICollUtil.isEmpty(specNames)) return attrModels;
        for (SpecName specName : specNames) {
            SpecAttrModel attrModel = SpecAttrModel.of(specName.getCat3Id())
                    .specName(specName.getId(), specName.getSpecName());
            attrModels.add(attrModel);
        }
        return attrModels;
    }

    @Override
    public void uniqueCheck(SpecName specName) {
        List<SpecName> specNames = this.listByCat3IdWithCache(specName.getCat3Id());
        if (specNames.isEmpty()) return;

        for (SpecName name : specNames) {
            if (name.equalsAndExcludeSelf(specName)) {
                throw new UniqueException();
            }
        }
    }

    @Override
    @CacheEvict(value = CacheConstants.SPEC_ATTR_NAME_CAT_CACHE, key = "#specName.getCat3Id()")
    public boolean removeWithCache(SpecName specName) {
        return this.removeById(specName.getId());
    }
}
