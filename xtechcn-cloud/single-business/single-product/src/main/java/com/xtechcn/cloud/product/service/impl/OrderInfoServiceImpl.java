package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.entity.OrderInfo;
import com.xtechcn.cloud.product.mapper.OrderInfoMapper;
import com.xtechcn.cloud.product.service.OrderInfoService;
import org.springframework.stereotype.Service;


/**
* 订单表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

}
