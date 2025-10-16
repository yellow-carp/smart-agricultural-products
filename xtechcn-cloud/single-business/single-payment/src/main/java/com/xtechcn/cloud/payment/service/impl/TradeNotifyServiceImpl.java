package com.xtechcn.cloud.payment.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.payment.entity.TradeNotify;
import com.xtechcn.cloud.payment.mapper.TradeNotifyMapper;
import com.xtechcn.cloud.payment.service.TradeNotifyService;
import org.springframework.stereotype.Service;

/**
 * 支付交易回调信息表
 *
 * @author Alay
 * @since 2022-05-16 10:17:37
 */
@Service
public class TradeNotifyServiceImpl extends ServiceImpl<TradeNotifyMapper, TradeNotify> implements TradeNotifyService {

}