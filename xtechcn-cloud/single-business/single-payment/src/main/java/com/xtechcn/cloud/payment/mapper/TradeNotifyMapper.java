package com.xtechcn.cloud.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtechcn.cloud.payment.entity.TradeNotify;
import org.apache.ibatis.annotations.Mapper;


/**
 * 支付交易回调信息表
 *
 * @author Alay
 * @since 2022-05-16 10:17:37
 */
@Mapper
public interface TradeNotifyMapper extends BaseMapper<TradeNotify> {

}
