package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.customer.constants.CacheConstants;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.entity.Unit;
import com.xtechcn.cloud.customer.mapper.AreaMapper;
import com.xtechcn.cloud.customer.model.dto.RegionDto;
import com.xtechcn.cloud.customer.model.po.AreaParam;
import com.xtechcn.cloud.customer.model.vo.AreaView;
import com.xtechcn.cloud.customer.service.AreaService;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.serialization.lang.JsonObj;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 区域管理
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final RedisTemplate redisTemplate;

    @Override
    public List<AreaView> returnViewHandler(List<Area> areaList) {
        List<AreaView> areaViewList = new ArrayList<>();
        // 避免传入null导致的异常
        if (areaList == null) {
            return areaViewList;
        }
        for (Area area : areaList) {
            AreaView areaView = new AreaView();
            areaView.setId(area.getId());
            areaView.setName(area.getName());
            areaView.setType(area.getType());
            areaView.setDeliveryModel(area.getDeliveryModel());
            areaView.setEnabled(area.getEnabled());
            areaView.setRegionFullPath(area.getRegionFullPath());
            // 处理区域名称
            String regionFullName = area.getRegionFullName().concat(" ");
            for (RegionDto regionDto : area.getRegionCodeList()) {
                regionFullName = regionFullName.concat(regionDto.getName()).concat(";");
            }
            areaView.setRegionFullName(area.getRegionFullName());
            areaView.setRegionCodeList(area.getRegionCodeList());
            areaViewList.add(areaView);
        }
        return areaViewList;
    }

    @Override
    public Area conventEntity(AreaParam param) {
        Area area = new Area();
        area.setId(param.getId());
        area.setName(param.getName());
        area.setType(param.getType());
        area.setDeliveryModel(param.getDeliveryModel());
        area.setRegionFullName(param.getRegionFullName());
        area.setRegionFullPath(param.getRegionFullPath());
        area.setRegionFullName(param.getRegionFullName());
        area.setRegionCodeList(param.getRegionCodeList());
        return area;
    }

    @Override
    public void uniqueCheck(Area area) {
        List<Area> areaList = this.list();
        if (ICollUtil.isEmpty(areaList)) return;
        for (Area ar : areaList) {
            if (ar.equalsAndExcludeSelf(area)) throw new UniqueException("该区域名称已存在");
        }
    }


    @Override
    @Cacheable(value = CacheConstants.AREA_CACHE_KEY, key = "#id", unless = "#result == null")
    public Area findAndCache(String id) {
        return this.getById(id);
    }

    @Override
    @CacheEvict(value = CacheConstants.AREA_CACHE_KEY, key = "#area.getId()")
    public boolean updateById(Area area) {
        return 1 == this.baseMapper.updateById(area);
    }

    @Override
    public void removeCache(String id) {
        redisTemplate.delete(CacheConstants.AREA_CACHE_KEY + "::" + id);
    }
}
