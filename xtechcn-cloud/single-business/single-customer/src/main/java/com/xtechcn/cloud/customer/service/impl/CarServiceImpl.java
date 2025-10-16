package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.customer.constants.CacheConstants;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.entity.Packaging;
import com.xtechcn.cloud.customer.mapper.CarMapper;
import com.xtechcn.cloud.customer.model.po.CarParam;
import com.xtechcn.cloud.customer.model.vo.CarPageView;
import com.xtechcn.cloud.customer.service.CarService;
import com.xtechcn.cloud.customer.service.PackagingService;
import com.xtechcn.common.core.exceptions.UniqueException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 车辆管理
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@Service
@RequiredArgsConstructor
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

    private final RedisTemplate redisTemplate;
    private final PackagingService packagingService;

    @Override
    public List<CarPageView> returnViewHandler(List<Car> carList) {
        if (carList.isEmpty()) return new ArrayList<>();
        List<CarPageView> unitViews = carList.stream().map((car) -> {
            CarPageView pageView = new CarPageView();
            pageView.setId(car.getId());
            pageView.setName(car.getName()+"("+car.getLength()+" x "+car.getWidth()+" x "+car.getHeight()+")");
            pageView.setEnabled(car.getEnabled());
            pageView.setType(car.getType());
            pageView.setLength(car.getLength());
            pageView.setWidth(car.getWidth());
            pageView.setHeight(car.getHeight());
            pageView.setWeight(car.getWeight());
            // 获取并设置容积
            pageView.setVolume(getVolume(car));
            // 获取并设置最大货物件数
            pageView.setMaxNum(getNumber(car, true));
            // 获取并设置最小货物件数
            pageView.setMinNum(getNumber(car, false));
            return pageView;
        }).collect(Collectors.toList());
        return unitViews;
    }

    private Double getVolume(Car car) {
        return car.getLength() * car.getWidth() * car.getHeight();
    }

    private Integer getNumber(Car car, Boolean b) {
        Double volume = this.getVolume(car);
        // 获取包装表最尺寸
        Double maxOrMin = this.MaxOrMin(b);
        if (maxOrMin == null) return null;
        int max = (int) Math.floor(volume / maxOrMin);
        return max;
    }

    private Double MaxOrMin(Boolean b) {
        List<Packaging> packagingList = packagingService.list();
        if (packagingList == null || packagingList.isEmpty()) {
            // 处理空列表情况，避免后续计算出错
            return null;
        }
        Double min = packagingList.get(0).getLength() * packagingList.get(0).getHeight() * packagingList.get(0).getWidth();
        Double max = min;
        for (Packaging packaging : packagingList) {
            // 单位换算保持一致为(立方米)
            Double volume = (packaging.getLength() * packaging.getHeight() * packaging.getWidth()) / 1000000;

            // 单独判断并更新最大值和最小值
            if (volume > max) {
                max = volume;
            }
            if (volume < min) {
                min = volume;
            }
        }

        if (b)
            return min;
        else
            return max;
    }

    @Override
    public Car conventEntity(CarParam carAddParam) {
        Car car = new Car();
        car.setId(carAddParam.getId());
        car.setName(carAddParam.getName());
        car.setWeight(carAddParam.getWeight());
        car.setLength(carAddParam.getLength());
        car.setWidth(carAddParam.getWidth());
        car.setHeight(carAddParam.getHeight());
        car.setType(carAddParam.getType());
        return car;
    }

    @Override
    public void updateNum(Car car) {
        // 获取最大货物件数
        car.setMinNum(getNumber(car, false));
        car.setMaxNum(getNumber(car, true));
    }

    @Override
    public void uniqueCheck(Car car) {
        List<Car> carList = this.list();
        if (carList.isEmpty()) return;
        for (Car grade : carList) {
            if (grade.equalsAndExcludeSelf(car)) {
                throw new UniqueException("已存在车辆信息");
            }
        }
    }

    @Override
    @Cacheable(value = CacheConstants.CAR_CACHE_KEY, key = "#id", unless = "#result == null")
    public Car findAndCache(String id) {
        return this.getById(id);
    }

    @Override
    @CacheEvict(value = CacheConstants.CAR_CACHE_KEY, key = "#entity.getId()")
    public boolean updateById(Car entity) {
        return 1 == this.baseMapper.updateById(entity);
    }

    @Override
    public void removeCache(String carId) {
        redisTemplate.delete(CacheConstants.CAR_CACHE_KEY + "::" + carId);
    }
}
