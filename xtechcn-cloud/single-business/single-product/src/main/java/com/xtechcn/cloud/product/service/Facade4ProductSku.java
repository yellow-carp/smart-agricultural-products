package com.xtechcn.cloud.product.service;

import com.xtechcn.cloud.product.entity.ProductSku;
import com.xtechcn.cloud.product.entity.SkuSaleAttr;
import com.xtechcn.cloud.product.model.InventoryModel;
import com.xtechcn.cloud.product.model.SaleAttrModel;
import com.xtechcn.cloud.product.model.po.ProductSkuMaParam;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.model.vo.ProductSkuMaView;
import com.xtechcn.cloud.product.model.vo.ProductSpuMaView;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.core.utils.ICombinationUtil;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.core.utils.IStrPool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 商品外观类
 *
 * @author Weijixiao
 * @since 2025-09-17 14:22
 */
@Component
@RequiredArgsConstructor
public class Facade4ProductSku {
    private final ProductSkuService productSkuService;
    private final SkuSaleAttrService skuSaleAttrService;

    public  void upsertSkuSaleAttrs(ProductSpuMaParam productSpuModel, ProductSkuMaParam productSkuParam) {
        skuSaleAttrService.upsertSkuSaleAttrs(productSkuParam, productSpuModel.getSaleAttrs());
    }

    public void upsertInventoryProcess(List<InventoryModel> inventoryModels) {

    }

    public void processProductSkuModels(ProductSpuMaView productMaView) {
        // SKU 信息
        List<ProductSku> productSkus = productSkuService.listBySpu(productMaView.getSpu());
        // SPU下所有SKU 的销售属性
        List<SkuSaleAttr> allSkuSaleAttrs = skuSaleAttrService.listSkuSaleAttrsBySpu(productMaView.getSpu());
        List<ProductSkuMaView> productSkuModels = convertAndProcessSaleAttrs(productSkus, productMaView.getSaleAttrs(), allSkuSaleAttrs);
        productMaView.setProductSkus(productSkuModels);
    }

    private List<ProductSkuMaView> convertAndProcessSaleAttrs(List<ProductSku> productSkus, List<SaleAttrModel> saleAttrModels, List<SkuSaleAttr> allSkuSaleAttrs) {
        List<ProductSkuMaView> productSkuModels = new ArrayList<>();


        if (ICollUtil.isEmpty(saleAttrModels)) {
            ProductSku productSku = productSkus.getFirst();
            // SkuExtraInfo skuExtraInfo = skuExtraInfoService.findWithCache(productSku.getSku());
            // // 单规格,且没有销售属性的请求下
            // productSkuModels.add(convertSkuView(productSku, skuExtraInfo));
            return productSkuModels;
        }

        // Sku 的销售属性进行分组
        Map<String, List<SkuSaleAttr>> skuSaleAttrsGroup = IMapUtil.group(allSkuSaleAttrs, SkuSaleAttr::getSku);

        Map<Long, SaleAttrModel.AttrValue> attrValueMap = new HashMap<>();
        Map<String, String> skuMapping = new HashMap<>();
        // Sku 数据转换为Map
        Map<String, ProductSku> productSkuMap = IMapUtil.coll2Map(productSkus, ProductSku::getSku);
        // List<SkuExtraInfo> skuExtraInfos = skuExtraInfoService.listBySkus(productSkuMap.keySet());
        // Map<String, SkuExtraInfo> extraInfoMap = IMapUtil.coll2Map(skuExtraInfos, SkuExtraInfo::getSku);

        // 每一个SKU进行一一处理 销售属性 key
        for (Map.Entry<String, List<SkuSaleAttr>> entry : skuSaleAttrsGroup.entrySet()) {
            String sku = entry.getKey();
            List<SkuSaleAttr> skuSaleAttrs = entry.getValue();
            // sku 销售属性转Map
            Map<Long, SkuSaleAttr> skuSaleAttrsMap = IMapUtil.coll2Map(skuSaleAttrs, SkuSaleAttr::getAttrId);
            StringBuilder attrKeyBuilder = new StringBuilder();
            for (SaleAttrModel saleAttrModel : saleAttrModels) {
                SkuSaleAttr skuSaleAttr = skuSaleAttrsMap.get(saleAttrModel.getAttrId());
                SaleAttrModel.AttrValue attrValue = attrValueMap.get(skuSaleAttr.getValueId());
                if (null == attrValue) {
                    attrValue = ICollUtil.findOne(saleAttrModel.getAttrValues(), SaleAttrModel.AttrValue::getValueId, skuSaleAttr.getValueId());
                    attrValueMap.put(skuSaleAttr.getValueId(), attrValue);
                }
                assert attrValue != null;
                attrKeyBuilder.append(attrValue.getAttrValue()).append(IStrPool.PIPE);
            }
            // 移除最后一个管道分隔符
            attrKeyBuilder.deleteCharAt(attrKeyBuilder.length() - 1);
            skuMapping.put(attrKeyBuilder.toString(), sku);
        }
        // 对象销售属性进行排列组合处理
        List<String> combinations = ICombinationUtil.sortCombinations(saleAttrModels, SaleAttrModel::getAttrValues, SaleAttrModel.AttrValue::getAttrValue, IStrPool.PIPE);

        for (String attrValueKey : combinations) {
            String sku = skuMapping.get(attrValueKey);
            ProductSku productSku = productSkuMap.get(sku);
            // SkuExtraInfo skuExtraInfo = extraInfoMap.get(sku);

            // 这个方案是如果销售属性没有对应真实存在的SKU,那么模拟生成一个空的SKU数据(二选一)
            // ProductSkuMaView productSkuModel = null != productSku ? convertSkuView(productSku, skuExtraInfo) : ProductSkuMaView.empty();

            // 如果没有对应真实存在的SKU,那么直接忽略(二选一)
            if (null == productSku) continue;
            // ProductSkuMaView productSkuModel = convertSkuView(productSku, skuExtraInfo);

            // productSkuModel.setSaleAttrs(attrValueKey);
            // productSkuModels.add(productSkuModel);
        }
        return productSkuModels;
    }

    public void updateOnRemove(ProductSpuMaParam productSpuModel) {
        Set<String> removeSkus = productSpuModel.removeSkus();
        if (removeSkus.isEmpty()) return;

        // 删除SKU
        productSkuService.removeByIds(removeSkus);
        // 删除SKU 关联的销售属性
        skuSaleAttrService.removeBySkuIds(productSpuModel.getSpu(), removeSkus);
        // 删除SKU 库存
        this.removeInventories(removeSkus);
    }
    private void removeInventories(Set<String> inventorySkus) {
    }

    public void removeAboutSkus(String spu, List<String> skuNos) {
        // 删除商品SKU扩展信息
        // skuExtraInfoService.removeBatchByIds(skuNos);
        // 删除sku 销售属性值
        skuSaleAttrService.removeBySkuIds(spu, skuNos);
    }
}
