package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.SpuExtraInfo;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.model.vo.ProductSpuMaView;

/**
 * 商品额外的信息
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
public interface SpuExtraInfoService extends IService<SpuExtraInfo> {
    /**
     * SPU 扩展信息查询
     */
    SpuExtraInfo findWithCache(String spu);

    /**
     * 商品视图数据装填
     */
    void fillDetailModels(ProductSpuMaView productMaView);

    /**
     * 参数转换实体
     */
    SpuExtraInfo convertEntity(ProductSpuMaParam productSpuModel);

    /**
     * 插入/更新初始化
     */
    void upsertInit(SpuExtraInfo spuExtraInfo);

}