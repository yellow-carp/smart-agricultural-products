package com.xtechcn.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.product.entity.ProductQueryParam;
import com.xtechcn.cloud.product.entity.ProductSpu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 商品SPU表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Mapper
public interface ProductSpuMapper extends BaseMapper<ProductSpu> {

    /**
     * 自定义的分页查询
     */
    Page<ProductSpu> selectPageQuery(Page page, @Param("query") ProductQueryParam query);

}