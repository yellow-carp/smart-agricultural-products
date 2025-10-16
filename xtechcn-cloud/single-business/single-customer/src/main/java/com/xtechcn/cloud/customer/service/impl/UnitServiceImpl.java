package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.customer.entity.Unit;
import com.xtechcn.cloud.customer.mapper.UnitMapper;
import com.xtechcn.cloud.customer.model.po.UnitPageParam;
import com.xtechcn.cloud.customer.model.po.UnitParam;
import com.xtechcn.cloud.customer.model.vo.UnitView;
import com.xtechcn.cloud.customer.service.UnitService;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.utils.ICollUtil;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
* 商品单位
*
* @author hanjie
* @since 2025-09-11 17:59:27
*/
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {
    @Override
    public void uniqueCheck(Unit unit) {
        List<Unit> units = this.list();
        if (units.isEmpty()) return;
        for (Unit ut : units) {
            if (ut.equalsAndExcludeSelf(unit)) {
                throw new UniqueException("已存在相同商品单位信息");
            }
        }
    }

    @Override
    public void upsertInit(Unit unit) {

    }

    @Override
    public Unit conventEntity(UnitParam unitParam) {
        Unit unit=new Unit();
        unit.setId(unitParam.getId());
        unit.setName(unitParam.getName());
        unit.setCommAmount(unitParam.getCommAmount());
        unit.setSort(unitParam.getSort());
        return unit;
    }

    @Override
    public void exist(Unit unit) {
        Unit ut = this.getById(unit.getId());
        if(ut==null) throw new UniqueException(FailedResult.NOT_FOUNT);
    }

    @Override
    public List<UnitView> returnViewHandler(List<Unit> records) {
        if(records.isEmpty()) return new ArrayList<>();
        List<UnitView> unitViews=records.stream().map((ut)->{
            UnitView unitView=new UnitView();
            unitView.setId(ut.getId());
            unitView.setName(ut.getName());
            unitView.setSort(ut.getSort());
            unitView.setCommAmount(ut.getCommAmount());
            unitView.setUpsertBy(ut.getUpsertBy());
            return unitView;
        }).collect(Collectors.toList());
        return unitViews;
    }

    @Override
    public Unit convent(UnitPageParam unitPageParam) {
        Unit unit=new Unit();
        unit.setName(unitPageParam.getName());
        unit.setCommAmount(unitPageParam.getCommAmount());
        return unit;
    }
}
