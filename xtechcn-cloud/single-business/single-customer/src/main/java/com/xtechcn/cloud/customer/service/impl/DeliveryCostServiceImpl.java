package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.customer.constants.CacheConstants;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.entity.DeliveryCost;
import com.xtechcn.cloud.customer.mapper.DeliveryCostMapper;
import com.xtechcn.cloud.customer.model.po.DeliveryCostParam;
import com.xtechcn.cloud.customer.model.vo.DeliveryCostView;
import com.xtechcn.cloud.customer.service.AreaService;
import com.xtechcn.cloud.customer.service.CarService;
import com.xtechcn.cloud.customer.service.DeliveryCostService;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.core.utils.IMapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 物流成本
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@Service
@RequiredArgsConstructor
public class DeliveryCostServiceImpl extends ServiceImpl<DeliveryCostMapper, DeliveryCost> implements DeliveryCostService {
    private final RedisTemplate redisTemplate;
    private final CarService carService;
    private final AreaService areaService;

    @Override
    public List<DeliveryCostView> returnViewHandler(List<DeliveryCost> deliveryCosts) {
        if (deliveryCosts.isEmpty()) return new ArrayList<>();
        List<String> strings2 = ICollUtil.process2List(deliveryCosts, DeliveryCost::getAreaOriginId);
        List<String> strings1 = ICollUtil.process2List(deliveryCosts, DeliveryCost::getAreaSalesId);
        List<String> strings = ICollUtil.process2List(deliveryCosts, DeliveryCost::getCarId);
        strings1.addAll(strings2);
        List<Car> cars = carService.listByIds(strings);
        List<Area> areas = areaService.listByIds(strings1);
        Map<String, Car> carMap = IMapUtil.coll2Map(cars, Car::getId);
        Map<String, Area> areaMap = IMapUtil.coll2Map(areas, Area::getId);

        return deliveryCosts.stream().map(deliveryCost -> {
            DeliveryCostView deliveryCostView = new DeliveryCostView();
            deliveryCostView.setId(deliveryCost.getId());
            deliveryCostView.setCarId(deliveryCost.getCarId());
            deliveryCostView.setCarName(carMap.get(deliveryCost.getCarId()).getName() + "(" + carMap.get(deliveryCost.getCarId()).getLength() + " x " + carMap.get(deliveryCost.getCarId()).getWidth() + " x " + carMap.get(deliveryCost.getCarId()).getHeight() + ")");
            deliveryCostView.setAreaOriginId(deliveryCost.getAreaOriginId());
            deliveryCostView.setAreaOriginName(areaMap.get(deliveryCost.getAreaOriginId()).getName());
            deliveryCostView.setAreaSalesId(deliveryCost.getAreaSalesId());
            deliveryCostView.setAreaSalesName(areaMap.get(deliveryCost.getAreaSalesId()).getName());
            deliveryCostView.setCost(deliveryCost.getCost());
            deliveryCostView.setRegionDistance(deliveryCost.getRegionDistance());
            deliveryCostView.setUnitCost(deliveryCost.getUnitCost());
            return deliveryCostView;
        }).collect(Collectors.toList());
    }

    @Override
    public DeliveryCost conventEntity(DeliveryCostParam deliveryCostParam) {
        DeliveryCost deliveryCost = new DeliveryCost();
        deliveryCost.setId(deliveryCostParam.getId());
        deliveryCost.setCarId(deliveryCostParam.getCarId());
        deliveryCost.setAreaOriginId(deliveryCostParam.getAreaOriginId());
        deliveryCost.setAreaSalesId(deliveryCostParam.getAreaSalesId());
        deliveryCost.setCost(deliveryCostParam.getCost());
        deliveryCost.setRegionDistance(deliveryCostParam.getRegionDistance());
        deliveryCost.setUnitCost(deliveryCostParam.getCost().divide(BigDecimal.valueOf(deliveryCostParam.getRegionDistance()), 6, RoundingMode.HALF_UP));
        return deliveryCost;
    }

    @Override
    public void uniqueCheck(DeliveryCost deliveryCost) {
        List<DeliveryCost> deliveryCosts = this.list();
        if (deliveryCosts.isEmpty()) return;
        for (DeliveryCost de : deliveryCosts) {
            if (de.equalsAndExcludeSelf(deliveryCost)) {
                throw new UniqueException("已存在物流成本信息");
            }
        }
    }

    @Override
    @Cacheable(value = CacheConstants.DELIVERYCOST_CACHE_KEY, key = "#id", unless = "#result == null")
    public DeliveryCost findAndCache(String id) {
        return this.getById(id);
    }

    @Override
    @CacheEvict(value = CacheConstants.DELIVERYCOST_CACHE_KEY, key = "#entity.getId()")
    public boolean updateById(DeliveryCost entity) {
        return 1 == this.baseMapper.updateById(entity);
    }

    @Override
    public void removeCache(String carId) {
        redisTemplate.delete(CacheConstants.DELIVERYCOST_CACHE_KEY + "::" + carId);
    }

    @Override
    public void exist(DeliveryCost deliveryCost) {
        if (areaService.getOne(Wrappers.<Area>lambdaQuery().eq(Area::getId, deliveryCost.getAreaOriginId())) == null) {
            throw new UniqueException("该产区不存在");
        }
        if (areaService.getOne(Wrappers.<Area>lambdaQuery().eq(Area::getId, deliveryCost.getAreaSalesId())) == null) {
            throw new UniqueException("该销区不存在");
        }
    }
}
