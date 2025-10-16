package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.OrderDetail;
import com.xtechcn.cloud.product.model.vo.OrderDetailVo;

import java.util.List;
import java.util.Set;

/**
* 订单详情
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
public interface OrderDetailService extends IService<OrderDetail> {

    /**
     * 根据订单号查询
     */
    List<OrderDetailVo> listByOrderNo(String orderNo);

    /**
     * 根据订单查询
     */
    List<OrderDetail> listByOrder(String orderNo);

    /**
     * 根据订单号和 sku 查询
     */
    List<OrderDetail> getSkus(String orderNo, Set<String> skus);

    /**
     * 根据订单号查询
     */
    List<OrderDetail> listByOrders(List<String> orderNos);

    /**
     * 根据sku查询
     */
    List<OrderDetail> listBySkus(List<String> skuNos);
}