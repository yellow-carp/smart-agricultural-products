package com.xtechcn.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtechcn.cloud.product.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
* 商品分类
*
* @author hanjie
* @since 2025-09-16 09:45:56
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}