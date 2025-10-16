package com.xtechcn.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtechcn.cloud.product.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

/**
* 购物车表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

}