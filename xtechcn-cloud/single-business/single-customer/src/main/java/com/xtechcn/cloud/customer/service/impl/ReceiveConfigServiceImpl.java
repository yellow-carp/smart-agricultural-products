package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.customer.constants.AreaTypeEnum;
import com.xtechcn.cloud.customer.constants.CacheConstants;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.entity.ReceiveConfig;
import com.xtechcn.cloud.customer.mapper.AreaMapper;
import com.xtechcn.cloud.customer.mapper.ReceiveConfigMapper;
import com.xtechcn.cloud.customer.model.po.ReceiveConfigParam;
import com.xtechcn.cloud.customer.model.vo.ReceiveConfigView;
import com.xtechcn.cloud.customer.service.ReceiveConfigService;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.core.utils.IMapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 自动收货配置
 *
 * @author hanjie
 * @since 2025-09-11 17:59:28
 */
@Service
@RequiredArgsConstructor
public class ReceiveConfigServiceImpl extends ServiceImpl<ReceiveConfigMapper, ReceiveConfig> implements ReceiveConfigService {

    private final AreaMapper areaMapper;
    private final RedisTemplate<String, ?> redisTemplate;

    @Async
    @Transactional
    @Override
    public void init(Area area) {
        List<Area> areas;
        // 判断新增的区域管理是产区还是销区（如产区就查询出所有的销区)
        boolean equals = Objects.equals(area.getType(), AreaTypeEnum.AREA_ORIGIN.getCode());
        if (equals) {
            // 查询所有销区
            areas = this.selectAllType(AreaTypeEnum.AREA_SALES.getCode());
        } else {
            areas = this.selectAllType(AreaTypeEnum.AREA_ORIGIN.getCode());
        }
        // 对当前区域管理进行排列组合,初始化自动收货配置参数
        List<ReceiveConfig> receiveConfigs = this.saveBatch(equals, area, areas);
        // 批量插入
        this.saveOrUpdateBatch(receiveConfigs);
    }

    private List<Area> selectAllType(Integer type) {
        return areaMapper.selectList(Wrappers.<Area>lambdaQuery().eq(Area::getType, type));
    }

    private List<ReceiveConfig> saveBatch(Boolean b, Area area, List<Area> areas) {
        return areas.stream().map(a -> {
            ReceiveConfig receiveConfig = new ReceiveConfig();
            if (b) {
                receiveConfig.setAreaOriginId(area.getId());
                receiveConfig.setAreaSalesId(a.getId());
            } else {
                receiveConfig.setAreaOriginId(a.getId());
                receiveConfig.setAreaSalesId(area.getId());
            }
            return receiveConfig;
        }).toList();
    }

    @Override
    public List<ReceiveConfigView> returnViewHandler(List<ReceiveConfig> receiveConfigList) {
        List<String> areaOriginIds = ICollUtil.process2List(receiveConfigList, ReceiveConfig::getAreaOriginId);
        List<String> areaSalesIds = ICollUtil.process2List(receiveConfigList, ReceiveConfig::getAreaSalesId);
        areaOriginIds.addAll(areaSalesIds);
        // 查询区域信息
        List<Area> areas = areaMapper.selectBatchIds(areaOriginIds);
        Map<String, Area> areaMap = IMapUtil.coll2Map(areas, Area::getId);
        // 封装数据
        return receiveConfigList.stream().map(receiveConfig -> {
            ReceiveConfigView receiveConfigView = new ReceiveConfigView();
            receiveConfigView.setId(receiveConfig.getId());
            receiveConfigView.setAreaOriginId(receiveConfig.getAreaOriginId());
            receiveConfigView.setAreaOriginName(areaMap.get(receiveConfig.getAreaOriginId()).getName());
            receiveConfigView.setAreaSalesId(receiveConfig.getAreaSalesId());
            receiveConfigView.setAreaSalesName(areaMap.get(receiveConfig.getAreaSalesId()).getName());
            receiveConfigView.setDay(receiveConfig.getDay());
            return receiveConfigView;
        }).toList();
    }

    @Override
    public ReceiveConfig conventEntity(ReceiveConfigParam receiveConfigParam) {
        ReceiveConfig receiveConfig = new ReceiveConfig();
        receiveConfig.setId(receiveConfigParam.getId());
        return receiveConfig.withId()
                .day(receiveConfigParam.getDay())
                .areaOriginId(receiveConfigParam.getAreaOriginId())
                .areaSalesId(receiveConfigParam.getAreaSalesId());
    }

    @Override
    public void uniqueCheck(ReceiveConfig receiveConfig) {
        List<ReceiveConfig> receiveConfigs = this.list();
        if (receiveConfigs.isEmpty()) return;
        for (ReceiveConfig rc : receiveConfigs) {
            if (rc.equalsAndExcludeSelf(receiveConfig)) {
                throw new UniqueException("已存在相同的自动收货信息");
            }
        }
    }

    @Override
    @Cacheable(value = CacheConstants.RECEIVECONFIG_CACHE_KEY, key = "#id", unless = "#result == null")
    public ReceiveConfig findAndCache(String id) {
        return this.getById(id);
    }

    @Override
    @CacheEvict(value = CacheConstants.RECEIVECONFIG_CACHE_KEY, key = "#entity.getId()")
    public boolean updateById(ReceiveConfig entity) {
        return 1 == this.baseMapper.updateById(entity);
    }

    @Override
    public void removeCache(String id) {
        redisTemplate.delete(CacheConstants.RECEIVECONFIG_CACHE_KEY + "::" + id);
    }
}
