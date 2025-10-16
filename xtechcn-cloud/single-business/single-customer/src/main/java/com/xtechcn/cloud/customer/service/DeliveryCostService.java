package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.entity.DeliveryCost;
import com.xtechcn.cloud.customer.model.po.CarParam;
import com.xtechcn.cloud.customer.model.po.DeliveryCostParam;
import com.xtechcn.cloud.customer.model.vo.CarPageView;
import com.xtechcn.cloud.customer.model.vo.DeliveryCostView;

import java.util.List;

/**
* 物流成本
*
* @author hanjie
* @since 2025-09-11 17:59:27
*/
public interface DeliveryCostService extends IService<DeliveryCost> {

    /**
     * 封装数据返回
     * @param deliveryCosts
     * @return
     */
    List<DeliveryCostView> returnViewHandler(List<DeliveryCost> deliveryCosts);

    /**
     * 参数转实体
     * @param deliveryCostParam
     * @return
     */
    DeliveryCost conventEntity(DeliveryCostParam deliveryCostParam);
    /**
     * 重复校验
     * @param deliveryCost
     */
    void uniqueCheck(DeliveryCost deliveryCost);
    /**
     * 查询缓存
     */
    DeliveryCost findAndCache(String id);

    /**
     * 删除缓存
     */
    void removeCache(String carId);

    /**
     * 判断该产区或销区是否存在
     * @param deliveryCost
     */
    void exist(DeliveryCost deliveryCost);
}