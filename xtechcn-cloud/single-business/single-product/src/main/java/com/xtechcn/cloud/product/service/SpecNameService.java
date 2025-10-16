package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.SpecName;
import com.xtechcn.cloud.product.model.SpecAttrModel;

import java.util.List;

/**
* spu规格名称
*
* @author hanjie
* @since 2025-09-16 09:45:56
*/
public interface SpecNameService extends IService<SpecName> {

    /**
     * 根据cat3Id判断是否有规格名称
     */
    boolean cat3IdHasSpecName(String cat3Id);

    /**
     * 根据cat3Id查询规格名称
     */
    List<SpecName> listByCat3IdWithCache(String cat3Id);

    /**
     * 视图数据转换
     */
    List<SpecAttrModel> convertSpecAttrModels(List<SpecName> specNames);

    /**
     * 数据唯一性检查
     */
    void uniqueCheck(SpecName specName);

    /**
     * 删除规格名以及缓存
     */
    boolean removeWithCache(SpecName specName);
}