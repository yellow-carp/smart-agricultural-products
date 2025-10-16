package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.ProductQueryParam;
import com.xtechcn.cloud.product.entity.ProductSpu;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.model.vo.ProductSpuMaView;

import java.util.List;

/**
 * 商品SPU表
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
public interface ProductSpuService extends IService<ProductSpu> {

    /**
     * 商品数据构建
     */
    ProductSpu convertEntity(ProductSpuMaParam productSpuModel);

    /**
     * 数据插入初始化处理
     */
    void upsertInit(ProductSpu productSpu);

    /**
     * 商品数据唯一性验证
     */
    void uniqueCheck(ProductSpu productSpu);

    /**
     * 分页查询商品(xml)
     */
    Page<ProductSpu> pageQuery(Page page, ProductQueryParam queryParam);


    /**
     * SPU 数据信息
     */
    ProductSpu findWithCache(String spu);

    /**
     * 数据返回处理
     */
    ProductSpuMaView returnValueHandler(ProductSpu productSpu);

    /**
     * 数据返回处理
     */
    List<ProductSpuMaView> returnValueHandler(List<ProductSpu> products);


    /**
     * 校验是否可删除
     */
    void removeVerify(ProductSpu productSpu);


}