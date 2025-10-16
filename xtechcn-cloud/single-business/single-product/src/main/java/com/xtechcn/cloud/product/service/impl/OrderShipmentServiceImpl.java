package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.entity.OrderShipment;
import com.xtechcn.cloud.product.mapper.OrderShipmentMapper;
import com.xtechcn.cloud.product.service.OrderShipmentService;
import org.springframework.stereotype.Service;


/**
* 订单-发货关系表
*
* @author hanjie
* @since 2025-09-16 09:45:56
*/
@Service
public class OrderShipmentServiceImpl extends ServiceImpl<OrderShipmentMapper, OrderShipment> implements OrderShipmentService {

}
