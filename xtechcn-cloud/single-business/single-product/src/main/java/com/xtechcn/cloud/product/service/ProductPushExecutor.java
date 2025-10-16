package com.xtechcn.cloud.product.service;

import com.xtechcn.cloud.product.entity.ProductSku;
import com.xtechcn.cloud.product.entity.ProductSpu;
import com.xtechcn.cloud.product.entity.SpuExtraInfo;
import com.xtechcn.cloud.product.model.InventoryModel;
import com.xtechcn.cloud.product.model.po.ProductSkuMaParam;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.utils.BoolOptional;
import com.xtechcn.common.core.utils.ICollUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weijixiao
 * @since 2025-09-17 14:57
 */
@Component
@RequiredArgsConstructor
public class ProductPushExecutor {

    private final Facade4ProductSpu facade4ProductSpu;
    private final Facade4ProductSku facade4ProductSku;
    private final ProductSpuService productSpuService;
    private final ProductSkuService productSkuService;
    private final SpuExtraInfoService spuExtraInfoService;
    /**
     * 执行商品发布
     */
    @Transactional
    public ProductSpuMaParam executePush(@Valid ProductSpuMaParam productSpuModel) {
        // 参数转换实体
        ProductSpu productSpu = productSpuService.convertEntity(productSpuModel);
        // 数据初始换处理
        productSpuService.  upsertInit(productSpu);
        // 商品数据唯一性验证
        productSpuService.uniqueCheck(productSpu);
        // 商品数据插入
        boolean isOk = productSpuModel.isAdd() ? productSpuService.save(productSpu) : productSpuService.updateById(productSpu);
        if (!isOk) throw new RollBackException();

        // 商品扩展表数据
        SpuExtraInfo spuExtraInfo = spuExtraInfoService.convertEntity(productSpuModel);
        // 初始化处理数据
        spuExtraInfoService.upsertInit(spuExtraInfo);
        isOk = productSpuModel.isAdd() ? spuExtraInfoService.save(spuExtraInfo) : spuExtraInfoService.updateById(spuExtraInfo);
        if (!isOk) throw new RollBackException();

        // 创建SPU规格属性添加(如果存在)
        BoolOptional.ifNotEmpty(productSpuModel.getSpecAttrs(), () -> facade4ProductSpu.upsertSpecValue(productSpuModel));
        // 创建SPU 销售属性(如果存在)
        BoolOptional.ifNotEmpty(productSpuModel.getSaleAttrs(), () -> facade4ProductSpu.upsertSaleAttrs(productSpuModel));
        // 创建SPU销售属值(如果存在)
        BoolOptional.ifNotEmpty(productSpuModel.getSaleAttrs(), () -> facade4ProductSpu.upsertSaleAttrsValue(productSpuModel));
        // 若SPU销售属不存在，则清除历史 销售属性 和 销售属值
        BoolOptional.ifTrue(ICollUtil.isEmpty(productSpuModel.getSaleAttrs()), () -> facade4ProductSpu.removeSaleAttrs(productSpuModel));

        // SKU 创建
        List<ProductSkuMaParam> productSkus = productSpuModel.getProductSkus();
        // SKU 库存数据
        List<InventoryModel> inventoryModels = new ArrayList<>();

        for (ProductSkuMaParam productSkuParam : productSkus) {
            // 参数转换实体
            ProductSku productSku = productSkuService.convertEntity(productSkuParam.spu(productSpuModel.getSpu()));
            // 不存在的SKU 不做处理(后台取消上架的SKU)
            if (null == productSku) continue;
            // 数据初始换处理
            productSkuService.upsertInit(productSpu, productSku);
            // 商品数据插入
            isOk = productSkuParam.isAdd() ? productSkuService.save(productSku) : productSkuService.updateById(productSku);

            // 创建SKU扩展表数据
            // if (isOk) {
            //     SkuExtraInfo skuExtraInfo = skuExtraInfoService.convertEntity(productSkuParam);
            //     // 数据入库
            //     isOk = productSkuParam.isAdd() ? skuExtraInfoService.save(skuExtraInfo) : skuExtraInfoService.updateById(skuExtraInfo);
            // }

            // 添加库存
            if (isOk) {
                inventoryModels.add(InventoryModel.of(productSpuModel.getSpu(), productSku.getSku())
                        .title(productSpuModel.getTitle().concat("#").concat(productSkuParam.getSaleAttrs()))
                        .quantity(productSkuParam.getInventory())
                );
                // 创建SKU 销售属性
                BoolOptional.ifNotEmpty(productSpuModel.getSaleAttrs(), () -> facade4ProductSku.upsertSkuSaleAttrs(productSpuModel, productSkuParam));
            }
        }
        // 添加库存
        facade4ProductSku.upsertInventoryProcess(inventoryModels);
        // 存在移除sku 的情况
        BoolOptional.ifTrue(!productSpuModel.isAdd(), () -> facade4ProductSku.updateOnRemove(productSpuModel));
        return productSpuModel;
    }
}
