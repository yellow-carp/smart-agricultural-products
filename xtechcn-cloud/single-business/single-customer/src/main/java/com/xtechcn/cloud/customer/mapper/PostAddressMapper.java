package com.xtechcn.cloud.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtechcn.cloud.customer.entity.PostAddress;
import org.apache.ibatis.annotations.Mapper;

/**
* 邮递地址信息表
*
* @author hanjie
* @since 2025-09-11 17:59:28
*/
@Mapper
public interface PostAddressMapper extends BaseMapper<PostAddress> {

}