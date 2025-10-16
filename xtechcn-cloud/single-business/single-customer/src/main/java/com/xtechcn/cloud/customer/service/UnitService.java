package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.Unit;
import com.xtechcn.cloud.customer.model.po.UnitPageParam;
import com.xtechcn.cloud.customer.model.po.UnitParam;
import com.xtechcn.cloud.customer.model.vo.UnitView;

import java.util.List;

/**
* 商品单位
*
* @author hanjie
* @since 2025-09-11 17:59:27
*/
public interface UnitService extends IService<Unit> {
    /**
     * 校验唯一性
     * @param unit
     */
    void uniqueCheck(Unit unit);

    /**
     * 新增初始化
     * @param unit
     */
    void upsertInit(Unit unit);

    /**
     * 参数转实体
     * @param unitParam
     * @return
     */
    Unit conventEntity(UnitParam unitParam);

    /**
     * 校验数据是否存在
     * @param unit
     */
    void exist(Unit unit);

    /**
     * 封装返回数据
     * @param unitList
     * @return
     */
    List<UnitView> returnViewHandler(List<Unit> unitList);

    Unit convent(UnitPageParam unitPageParam);
}