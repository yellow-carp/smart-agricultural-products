package com.xtechcn.cloud.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtechcn.cloud.payment.entity.Trade;
import org.apache.ibatis.annotations.Mapper;


/**
 * 交易信息表
 *
 * @author Alay
 * @since 2022-05-16 10:17:37
 */
@Mapper
public interface TradeMapper extends BaseMapper<Trade> {

}
