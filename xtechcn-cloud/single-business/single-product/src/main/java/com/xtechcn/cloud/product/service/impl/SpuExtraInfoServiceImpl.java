package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.entity.SpuExtraInfo;
import com.xtechcn.cloud.product.mapper.SpuExtraInfoMapper;
import com.xtechcn.cloud.product.model.po.ProductSkuMaParam;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.model.vo.ProductSpuMaView;
import com.xtechcn.cloud.product.service.SpuExtraInfoService;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.utils.BoolOptional;
import com.xtechcn.common.core.utils.IStrPool;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
* 商品额外的信息
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Service
public class SpuExtraInfoServiceImpl extends ServiceImpl<SpuExtraInfoMapper, SpuExtraInfo> implements SpuExtraInfoService {

    @Override
    @Cacheable(value = CacheConstants.PRODUCT_SPU_EXTRA_CACHE, key = "#spu", unless = "#result==null")
    public SpuExtraInfo findWithCache(String spu) {
        return this.getById(spu);
    }

    @Override
    public void fillDetailModels(ProductSpuMaView detailView) {
        SpuExtraInfo extraInfo = this.getById(detailView.getSpu());
        if (null == extraInfo) return;
        detailView.setDetails(extraInfo.getDetails());
        detailView.setCheckMsg(extraInfo.getCheckMsg());
        detailView.setImages(extraInfo.getImages());
        // detailView.setFreightId(extraInfo.getFreightId());
        detailView.setLimitMin(extraInfo.getLimitMin());
        // detailView.setTaxCode(extraInfo.getTaxCode());
    }

    @Override
    public SpuExtraInfo convertEntity(ProductSpuMaParam productSpuModel) {
        SpuExtraInfo spuExtraInfo = SpuExtraInfo.ofSpu(productSpuModel.getSpu());

        // 计算价格最高价最低价
        List<ProductSkuMaParam> productSkus = productSpuModel.getProductSkus();
        StringBuilder sb = new StringBuilder();
        // 单SKU 的商品
        if (productSpuModel.isSingleSku()) {
            ProductSkuMaParam single = productSpuModel.singleSku();
            sb.append(single.getUnitPrice()).append(IStrPool.COMMA).append(single.getUnitPrice());
        } else {
            List<ProductSkuMaParam> sortedProductSkus = productSkus.stream().sorted(Comparator.comparing(ProductSkuMaParam::getUnitPrice)).toList();
            // 最低价
            MoneyPenny lowPrice = sortedProductSkus.getFirst().getUnitPrice();
            // 最高价
            MoneyPenny highPrice = sortedProductSkus.getLast().getUnitPrice();
            sb.append(lowPrice).append(IStrPool.COMMA).append(highPrice);
        }
        // 价格区间范围
        // spuExtraInfo.setPriceRange(sb.toString());
        spuExtraInfo.setIsShowOnly(productSpuModel.getIsShowOnly());

        spuExtraInfo.setIsPublic(productSpuModel.getIsPublic());
        spuExtraInfo.setDeptIds(productSpuModel.getDeptIds());
        spuExtraInfo.setDetails(productSpuModel.getDetails());
        spuExtraInfo.setImages(productSpuModel.getImages());
        // spuExtraInfo.setFreightId(productSpuModel.getFreightId());
        spuExtraInfo.setLimitMin(productSpuModel.getLimitMin());
        // spuExtraInfo.setTaxCode(productSpuModel.getTaxCode());
        return spuExtraInfo;
    }

    @Override
    @CacheEvict(value = CacheConstants.PRODUCT_SPU_EXTRA_CACHE, key = "#spuExtraInfo.getSpu()")
    public void upsertInit(SpuExtraInfo spuExtraInfo) {
        // 最小购买数,默认处理为 1
        BoolOptional.ifTrue(null == spuExtraInfo.getLimitMin() || 0 == spuExtraInfo.getLimitMin(), () -> spuExtraInfo.setLimitMin(1));
    }
}
