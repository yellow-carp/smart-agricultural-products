package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.model.po.CarParam;
import com.xtechcn.cloud.customer.model.vo.CarPageView;

import java.util.List;

/**
* 车辆管理
*
* @author hanjie
* @since 2025-09-11 17:59:27
*/
public interface CarService extends IService<Car> {
    /**
     * 封装数据返回
     * @param carList
     * @return
     */
    List<CarPageView> returnViewHandler(List<Car> carList);

    /**
     * 参数转实体
     * @param carAddParam
     * @return
     */
    Car conventEntity(CarParam carAddParam);

    /**
     * 重新修改最大最小货物件数
     * @param car
     */
    void updateNum(Car car);

    /**
     * 重复校验
     * @param car
     */
    void uniqueCheck(Car car);

    /**
     * 查询车型
     */
    Car findAndCache(String id);

    /**
     * 删除缓存
     */
    void removeCache(String carId);
}