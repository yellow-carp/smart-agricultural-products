package com.xtechcn.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtechcn.cloud.product.entity.OrderShipment;
import org.apache.ibatis.annotations.Mapper;

/**
* 订单-发货关系表
*
* @author hanjie
* @since 2025-09-16 09:45:56
*/
@Mapper
public interface OrderShipmentMapper extends BaseMapper<OrderShipment> {

}