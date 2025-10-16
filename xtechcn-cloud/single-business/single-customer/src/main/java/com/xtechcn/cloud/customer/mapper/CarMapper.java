package com.xtechcn.cloud.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtechcn.cloud.customer.entity.Car;
import org.apache.ibatis.annotations.Mapper;

/**
* 车辆管理
*
* @author hanjie
* @since 2025-09-11 17:59:27
*/
@Mapper
public interface CarMapper extends BaseMapper<Car> {

}