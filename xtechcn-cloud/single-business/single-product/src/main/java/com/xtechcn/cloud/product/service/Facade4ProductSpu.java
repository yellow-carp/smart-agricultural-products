package com.xtechcn.cloud.product.service;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.xtechcn.cloud.product.constants.ProductConstants;
import com.xtechcn.cloud.product.constants.ProductResult;
import com.xtechcn.cloud.product.constants.ProductStateEnum;
import com.xtechcn.cloud.product.entity.*;
import com.xtechcn.cloud.product.model.SaleAttrModel;
import com.xtechcn.cloud.product.model.SpecAttrModel;
import com.xtechcn.cloud.product.model.po.ProductSkuMaParam;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.model.vo.ProductSpuMaView;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 商品外观类
 *
 * @author Weijixiao
 * @since 2025-09-17 14:04
 */
@Component
@RequiredArgsConstructor
public class Facade4ProductSpu {

    private final SpecNameService specNameService;
    private final SpuSpecValueService spuSpecValueService;
    private final SpuSaleAttrService spuSaleAttrService;
    private final SpuSaleAttrValueService spuSaleAttrValueService;
    private final ProductSpuService productSpuService;
    private final ProductSkuService productSkuService;
    private final SkuSaleAttrService skuSaleAttrService;

    public void processSpecAttrModels(ProductSpuMaView productMaView) {
        // 分类下的所有规格属性信息
        List<SpecName> specNames = specNameService.listByCat3IdWithCache(productMaView.getCat3Id());
        // Spu 下所有的规格数据信息
        List<SpuSpecValue> spuSpecValues = spuSpecValueService.listSpecAttrValuesWithCache(productMaView.getSpu());
        List<SpecAttrModel> specAttrs = convertSpecAttrViewModels(specNames, spuSpecValues);
        productMaView.setSpecAttrs(specAttrs);
    }

    private static List<SpecAttrModel> convertSpecAttrViewModels(List<SpecName> specNames, List<SpuSpecValue> spuSpecValues) {
        // SPU 属性值以Map 的形式转换
        Map<Long, SpuSpecValue> spuSpecValueMap = IMapUtil.coll2Map(spuSpecValues, SpuSpecValue::getNameId);
        // 规格数据返回处理
        List<SpecAttrModel> specViewModels = new ArrayList<>();

        for (SpecName specName : specNames) {
            SpecAttrModel specAttrModel = SpecAttrModel.of(specName.getCat3Id())
                    .specName(specName.getId(), specName.getSpecName());
            SpuSpecValue spuSpecValue = spuSpecValueMap.get(specName.getId());
            if (null != spuSpecValue) {
                specAttrModel.specValue(spuSpecValue.getId(), spuSpecValue.getSpecValue());
            }
            specViewModels.add(specAttrModel);
        }
        return specViewModels;
    }

    public void processSaleAttrModels(ProductSpuMaView productMaView) {
        // 销售属性
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrService.listSpuSaleAttrsWithCache(productMaView.getSpu());
        List<SpuSaleAttrValue> spuSaleAttrValues = spuSaleAttrValueService.listSpuSaleAttrValuesWithCache(productMaView.getSpu());
        List<SaleAttrModel> saleAttrModels = convertSaleAttrViewModels(spuSaleAttrs, spuSaleAttrValues);
        productMaView.setSaleAttrs(saleAttrModels);
    }

    private static List<SaleAttrModel> convertSaleAttrViewModels(List<SpuSaleAttr> spuSaleAttrs, List<SpuSaleAttrValue> spuSaleAttrValues) {
        if (null == spuSaleAttrs || spuSaleAttrs.isEmpty()) return new ArrayList<>();

        // 需要排序
        spuSaleAttrs.sort(Comparator.comparingInt(SpuSaleAttr::getSort));
        spuSaleAttrValues.sort(Comparator.comparingInt(SpuSaleAttrValue::getSort));
        List<SaleAttrModel> saleAttrModels = new ArrayList<>();
        Map<Long, List<SpuSaleAttrValue>> valuesMap = IMapUtil.group(spuSaleAttrValues, SpuSaleAttrValue::getAttrId);
        // 遍历每一组销售属性
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrs) {
            SaleAttrModel saleAttrModel = SaleAttrModel.create(spuSaleAttr.getId(), spuSaleAttr.getName());
            List<SpuSaleAttrValue> attrValues = valuesMap.get(spuSaleAttr.getId());
            // 遍历每一组销售属性值
            for (SpuSaleAttrValue saleAttrValue : attrValues) {
                saleAttrModel.addValue(saleAttrValue.getId(), saleAttrValue.getAttrValue())
                        .image(saleAttrValue.getImage());
            }
            saleAttrModels.add(saleAttrModel);
        }
        return saleAttrModels;
    }

    public void preProcessHandler(ProductSpuMaParam productSpuModel) {
        // 规格值预处理
        productSpuModel.getProductSkus().forEach(productSku -> {
            String saleAttrs = productSku.getSaleAttrs();
            saleAttrs = StrUtil.trim(saleAttrs);
            productSku.setSaleAttrs(saleAttrs.trim().replace(StrPool.CR, " ")
                    .replace(StrPool.LF, " ").replace(StrPool.TAB, " ").replace(IStrPool.PIPE, " "));
        });

        productSpuModel.getSaleAttrs().forEach(saleAttr -> {
            saleAttr.getAttrValues().forEach(attrValue -> {
                String value = attrValue.getAttrValue();
                value = StrUtil.trim(value);
                attrValue.setAttrValue(value.trim().replace(StrPool.CR, " ")
                        .replace(StrPool.LF, " ").replace(StrPool.TAB, " ").replace(IStrPool.PIPE, " "));
            });
        });
    }

    public void pushParameterVerify(ProductSpuMaParam productSpuModel) {
        if (IStrUtil.isBlank(productSpuModel.getAreaId())) {
            throw new ParamException("上架渠道未勾选");
        }
        // 过滤出有库存的商品,清洗无库存的商品
        List<ProductSkuMaParam> productSkuMaParams = ICollUtil.filter2List(productSpuModel.getProductSkus(), IPredicates.isEqual(true, ProductSkuMaParam::isEffective));
        productSpuModel.setProductSkus(productSkuMaParams);

        // 新增时,至少一个SKU存在库存
        if (productSkuMaParams.isEmpty()) throw new ParamException("一个SPU下至少需要一个有效的SKU数据");
        // 插入时参数验证
        if (productSpuModel.isAdd()) this.insertCheck(productSpuModel);
        // 修改时参数验证
        if (!productSpuModel.isAdd()) this.updateCheck(productSpuModel);
        // 规格属性值验证
        this.specAttrValuesCheck(productSpuModel);
        // SPU 与 SKU 中销售属性需要验证
        this.saleAttrValuesCheck(productSpuModel);
    }
    private void insertCheck(ProductSpuMaParam productSpuModel) {
        // 运费模板Id处理
        if (null == productSpuModel.getFreightId()) {
            // 默认使用默认运费模板
            productSpuModel.setFreightId(1);
        }
        List<ProductSkuMaParam> productSkus = productSpuModel.getProductSkus();
        for (ProductSkuMaParam productSkuModel : productSkus) {
            if (null == productSkuModel.getWeight()) {
                // sku 重量默认为 1.0
                productSkuModel.setWeight(1.0);
            }
        }
    }
    private void updateCheck(ProductSpuMaParam productSpuModel) {
        ProductSpu spuInDb = productSpuService.getById(productSpuModel.getSpu());
        ProductStateEnum state = ProductStateEnum.codeOf(spuInDb.getState());
        if (state.ge(ProductStateEnum.UP)) {
            // 上架状态的商品不允许修改
            throw new ParamException(ProductResult.PRODUCT_UP_STATE);
        }
        // 已经存在的SKU
        List<ProductSku> existProductSkus = productSkuService.listBySpu(productSpuModel.getSpu());
        Map<String, ProductSku> productSkuMap = IMapUtil.coll2Map(existProductSkus, ProductSku::getSku);
        List<ProductSkuMaParam> productSkus = productSpuModel.getProductSkus();
        for (ProductSkuMaParam productSkuModel : productSkus) {
            String sku = productSkuModel.getSku();
            if (null == sku || sku.isEmpty()) continue;
            productSkuMap.remove(sku);
        }
        // 需要移除的SKU
        productSpuModel.removeSkus(productSkuMap.keySet());
    }
    private void specAttrValuesCheck(ProductSpuMaParam productSpuModel) {
        List<SpecAttrModel> specAttrModels = productSpuModel.getSpecAttrs();
        if (ICollUtil.isEmpty(specAttrModels)) return;
        for (SpecAttrModel specAttrModel : specAttrModels) {
            String specValue = specAttrModel.getSpecValue();
            if (null == specValue) continue;
            // 首尾去除空格
            specValue = StrUtil.trim(specValue);
            if (specValue.length() > ProductConstants.ATTR_VALUE_MAX_LENGTH) {
                throw new ParamException("规格属性值: [ %s ]太长了", specValue);
            }
            specAttrModel.setSpecValue(specValue);
        }
    }
    private void saleAttrValuesCheck(ProductSpuMaParam productSpuModel) {
        List<SaleAttrModel> saleAttrs = productSpuModel.getSaleAttrs();
        if (ICollUtil.isEmpty(saleAttrs)) return;

        // 销售属性层级不允许超过 3 层
        if (saleAttrs.size() > ProductConstants.SALE_ATTR_MAX_LEVEL) {
            throw new ParamException(ProductResult.SALE_ATTR_LEVEL_TOO_HIGH);
        }
        // 计算销售属性值组合sku 数量
        int maxValueSize = 1;
        for (SaleAttrModel saleAttr : saleAttrs) {
            int valueSize = saleAttr.getAttrValues().size();
            maxValueSize *= valueSize;
        }
        if (maxValueSize > ProductConstants.SALE_ATTR_MAX_SIZE) {
            // 销售属性的值组合的SKU 不允许超过30个
            throw new ParamException(ProductResult.SALE_ATTR_SKU_TOO_MUCH);
        }
        // 对每个SKU 销售属性进行验证
        List<ProductSkuMaParam> productSkus = productSpuModel.getProductSkus();
        // SKU 数不能大于销售属性值排列组合的SKU数
        if (productSkus.size() > maxValueSize) throw new ParamException(ProductResult.SALE_ATTR_REPEAT);

        // 验证SKU中销售属性值重复性的Set
        Set<String> saleAttrValues = new HashSet<>();
        boolean isValid;
        for (ProductSkuMaParam skuParamModel : productSkus) {
            // SKU 的销售属性值
            String skuSaleValueStr = skuParamModel.getSaleAttrs();
            saleAttrValues.add(skuSaleValueStr);
            String[] skuSaleValues = StrUtil.splitToArray(skuSaleValueStr, IStrPool.PIPE);
            // 首尾去除空格
            StrUtil.trim(skuSaleValues);
            skuParamModel.setSaleAttrs(IStrUtil.arr2Str(skuSaleValues, IStrPool.PIPE));

            // SKU 销售属性值必须与SPU 中保持一致
            if (skuSaleValues.length != saleAttrs.size()) {
                throw new ParamException(ProductResult.SALE_ATTR_NUM_UN_MATCH);
            }

            for (int i = 0; i < saleAttrs.size(); i++) {
                isValid = false;
                SaleAttrModel saleAttrModel = saleAttrs.get(i);
                // SKU销售属性值
                String saleAttrValue = skuSaleValues[i].replace(StrPool.CR, " ")
                        .replace(StrPool.LF, " ").replace(StrPool.TAB, " ");
                if (ProductConstants.ATTR_VALUE_MAX_LENGTH < saleAttrValue.length()) {
                    // 属性值太长了
                    throw new ParamException(String.format("销售属性值: [ %s ]太长了", saleAttrValue));
                }
                // 遍历每一组销售属性值
                for (SaleAttrModel.AttrValue attrValue : saleAttrModel.getAttrValues()) {
                    // 首尾去除空格
                    String trim = attrValue.getAttrValue().trim();
                    if (Objects.equals(saleAttrValue, trim)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    throw new ParamException(String.format("销售属性: [ %s ]信息无效", saleAttrValue));
                }
            }
        }
        // 对SPU 销售属性信息进行排列组合,SKU 数量一点不能超过排列组合的数量
        if (saleAttrValues.size() != productSkus.size()) {
            throw new ParamException(ProductResult.SKU_SALE_ATTR_VALUE_INVALID);
        }
    }

    public void upsertSpecValue(ProductSpuMaParam productSpuModel) {
        spuSpecValueService.upsertSpecValue(productSpuModel);
    }

    public void upsertSaleAttrs(ProductSpuMaParam productSpuModel) {
        spuSaleAttrService.upsertSaleAttrs(productSpuModel);
    }

    public void upsertSaleAttrsValue(ProductSpuMaParam productSpuModel) {
        spuSaleAttrValueService.upsertSaleAttrsValue(productSpuModel);
    }

    public void removeSaleAttrs(ProductSpuMaParam productSpuModel) {
        spuSaleAttrService.removeSaleAttrs(productSpuModel);
        spuSaleAttrValueService.removeSaleAttrsValue(productSpuModel);
    }

    public void removeAboutSkus(String spu, List<String> skuNos) {
        // 删除商品SKU扩展信息
        // skuExtraInfoService.removeBatchByIds(skuNos);
        // 删除sku 销售属性值
        skuSaleAttrService.removeBySkuIds(spu, skuNos);
    }

    public void removeAboutSpu(String spu) {

    }
}
