package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.Packaging;
import com.xtechcn.cloud.customer.model.po.PackagingParam;
import com.xtechcn.cloud.customer.model.vo.PackagingView;

import java.util.List;

/**
* 包装形式
*
* @author hanjie
* @since 2025-09-11 17:59:28
*/
public interface PackagingService extends IService<Packaging> {
    /**
     * 参数转实体
     * @param packagingParam
     * @return
     */
    Packaging conventEntity(PackagingParam packagingParam);

    /**
     * 校验唯一性
     * @param packaging
     */
    void uniqueCheck(Packaging packaging);

    /**
     * 封装数据返回
     * @param packagingList
     * @return
     */
    List<PackagingView> returnViewHandler(List<Packaging> packagingList);

    /**
     * 校验是否存在该数据
     * @param packaging
     */
    void exist(Packaging packaging);

    /**
     * 查询缓存
     * @param id
     * @return
     */
    Packaging findAndCache(String id);

    /**
     * 更新缓存
     * @param entity
     * @return
     */
    boolean updateById(Packaging entity);

    /**
     * 删除缓存
     * @param packagingId
     */
    void removeCache(String packagingId);
}