package com.xtechcn.cloud.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.entity.OrderDetail;
import com.xtechcn.cloud.product.mapper.OrderDetailMapper;
import com.xtechcn.cloud.product.model.vo.OrderDetailVo;
import com.xtechcn.cloud.product.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * 订单详情
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Override
    public List<OrderDetailVo> listByOrderNo(String orderNo) {
        List<OrderDetail> orderDetails = this.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderNo, orderNo)
                .ne(OrderDetail::getNum, 0)
        );
        List<OrderDetailVo> orderDetailVos = BeanUtil.copyToList(orderDetails, OrderDetailVo.class);
        orderDetailVos.forEach(orderDetailVo -> {
            orderDetailVo.setTotalPrice(orderDetailVo.getPrice().multiply(orderDetailVo.getNum()));
        });
        return orderDetailVos;
    }

    @Override
    public List<OrderDetail> listByOrder(String orderNo) {
        return this.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderNo, orderNo));
    }

    @Override
    public List<OrderDetail> getSkus(String orderNo, Set<String> skus) {
        return this.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderNo, orderNo).in(OrderDetail::getSku, skus));
    }

    @Override
    public List<OrderDetail> listByOrders(List<String> orderNos) {
        return this.list(Wrappers.<OrderDetail>lambdaQuery().in(OrderDetail::getOrderNo, orderNos)
                .ne(OrderDetail::getNum, 0)
        );
    }

    @Override
    public List<OrderDetail> listBySkus(List<String> skuNos) {
        return this.list(Wrappers.<OrderDetail>lambdaQuery().in(OrderDetail::getSku, skuNos));
    }
}
