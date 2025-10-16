package com.xtechcn.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.constants.ProductConstants;
import com.xtechcn.cloud.product.entity.ProductSku;
import com.xtechcn.cloud.product.entity.ProductSpu;
import com.xtechcn.cloud.product.mapper.ProductSkuMapper;
import com.xtechcn.cloud.product.model.po.ProductSkuMaParam;
import com.xtechcn.cloud.product.service.ProductSkuService;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.utils.IParamUtil;
import com.xtechcn.common.sequence.core.XSequenceUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 商品SKU
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements ProductSkuService {

    @Override
    public ProductSku convertEntity(ProductSkuMaParam productSkuParam) {
        if (productSkuParam.getJdPrice().lt(productSkuParam.getUnitPrice())) {
            throw new ParamException("京东价不能低于含税单价");
        }
        // 标记参数是新增或者编辑
        productSkuParam.isAdd(null == productSkuParam.getSku());

        // 无效的SKU 不需要上架
        if (!productSkuParam.isEffective()) return null;

        // 新增时,SKU 参数需要生成
        IParamUtil.setStrProperty(productSkuParam, ProductSkuMaParam::setSku, productSkuParam.getSku(), this::skuGenerate);
        ProductSku productSku = new ProductSku();
        productSku.setSpu(productSkuParam.getSpu());
        productSku.setSku(productSkuParam.getSku());
        // SKU 主图
        productSku.setPoster(IParamUtil.ofStrValue(productSkuParam.getPoster(), (String) null));
        productSku.setUpc(productSkuParam.getUpc());
        // 含税单价(净价)
        productSku.setUnitPrice(productSkuParam.getUnitPrice());
        // 市场价
        productSku.setMarketPrice(productSkuParam.getMarketPrice());
        // 重量
        // productSku.setWeight(productSkuParam.getWeight());
        // 库存
        productSku.setQuantity(productSkuParam.getInventory());

        return productSku;
    }


    @Override
    @CacheEvict(value = CacheConstants.PRODUCT_SKU_CACHE, key = "#productSku.getSku()")
    public void upsertInit(ProductSpu productSpu, ProductSku productSku) {
        // SPU 和SKU 图片不可以同时为空
        Assert.hasText(productSpu.getPoster() + productSku.getPoster(), "商品主图不可为空");
        // Assert.isTrue(!(ICollUtil.isEmpty(productSpu.getImages()) && ICollUtil.isEmpty(productSku.getImages())), "商品多图不可为空");
        // 分类编码跟随SPU
        productSku.setCat3Id(productSpu.getCat3Id());
        // 品牌编码跟随SPU
        // productSku.setBrandId(productSpu.getBrandId());
        // 价格计算
        this.calculatePrice(productSpu, productSku);
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCT_SKU_CACHE, key = "#sku", unless = "#result==null")
    public ProductSku findWithCache(String sku) {
        return this.getById(sku);
    }

    @Override
    public List<ProductSku> listBySpu(String spu) {
        return this.list(Wrappers.<ProductSku>lambdaQuery().eq(ProductSku::getSpu, spu));
    }

    private String skuGenerate() {
        // Redis 自增ID
        return ProductConstants.SKU_PREFIX + XSequenceUtil.incrementNo(ProductConstants.SKU_NO_INCREMENT_KEY);
    }

    private void calculatePrice(ProductSpu productSpu, ProductSku productSku) {
        // 含税单价
        MoneyPenny unitPrice = productSku.getUnitPrice();
        if (MoneyPenny.ZERO.ge(unitPrice)) {
            throw new ParamException("含税单价不能为 0");
        }
        // 市场价（如果没有填写，默认净价基础多20%）
        MoneyPenny marketPrice = IParamUtil.ofValue(productSku.getMarketPrice(), unitPrice.multiply(ProductConstants.MARKET_RATE));
        if (marketPrice.lt(unitPrice)) {
            throw new ParamException("市场价不能低于含税单价");
        }
        productSku.setMarketPrice(marketPrice);
        // 净价
        // productSku.setNetPrice(unitPrice.divide(productSpu.getTaxRate().add(BigDecimal.ONE), 0, RoundingMode.HALF_UP));
        // // 税额 = 含税单价 - 净价
        // productSku.setTaxAmount(unitPrice.subtract(productSku.getNetPrice()));
    }
}
