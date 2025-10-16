package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.entity.Cart;
import com.xtechcn.cloud.product.mapper.CartMapper;
import com.xtechcn.cloud.product.service.CartService;
import org.springframework.stereotype.Service;


/**
* 购物车表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

}
