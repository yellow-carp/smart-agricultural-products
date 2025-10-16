package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.model.po.AreaParam;
import com.xtechcn.cloud.customer.model.vo.AreaView;

import java.util.List;

/**
* 区域管理
*
* @author hanjie
* @since 2025-09-11 17:59:27
*/
public interface AreaService extends IService<Area> {
    /**
     * 封装数据返回
     * @param areaList
     * @return
     */
    List<AreaView> returnViewHandler(List<Area> areaList);

    /**
     * 参数转实体
     * @param param
     * @return
     */
    Area conventEntity(AreaParam param);

    /**
     * 唯一性校验
     * @param area
     */
    void uniqueCheck(Area area);

    /**
     * 缓存查询
     * @param id
     * @return
     */
    Area findAndCache(String id);

    /**
     * 缓存删除
     * @param areaId
     */
    void removeCache(String areaId);
}