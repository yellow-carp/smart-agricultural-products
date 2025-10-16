package com.xtechcn.cloud.product.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.constants.ProductConstants;
import com.xtechcn.cloud.product.constants.ProductResult;
import com.xtechcn.cloud.product.constants.ProductStateEnum;
import com.xtechcn.cloud.product.entity.*;
import com.xtechcn.cloud.product.mapper.ProductSpuMapper;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.model.vo.CategoryView;
import com.xtechcn.cloud.product.model.vo.ProductSkuMaView;
import com.xtechcn.cloud.product.model.vo.ProductSpuMaView;
import com.xtechcn.cloud.product.service.*;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.core.utils.IParamUtil;
import com.xtechcn.common.core.utils.IStrPool;
import com.xtechcn.common.sequence.core.XSequenceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;


/**
* 商品SPU表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Service
@RequiredArgsConstructor
public class ProductSpuServiceImpl extends ServiceImpl<ProductSpuMapper, ProductSpu> implements ProductSpuService {

    private final SpuExtraInfoService spuExtraInfoService;
    private final CategoryService categoryService;
    private final ProductSkuService productSkuService;
    private final OrderDetailService orderDetailService;
    @Override
    public Page<ProductSpu> pageQuery(Page page, ProductQueryParam queryParam) {
        return this.baseMapper.selectPageQuery(page, queryParam);
    }


    @Override
    public ProductSpuMaView returnValueHandler(ProductSpu productSpu) {
        SpuExtraInfo extraInfo = spuExtraInfoService.findWithCache(productSpu.getSpu());
        ProductSpuMaView spuMaView = convert(productSpu, extraInfo);
        // 品牌数据
        // Brand brand = brandService.findWithCache(productSpu.getBrandId());
        //spuMaView.setBrand(BrandView.create(brand.getId(), brand.getCnName(), brand.getLogo()));
        // 分类数据
        List<Category> categories = categoryService.listWithCache(List.of(productSpu.getCat1Id(), productSpu.getCat2Id(), productSpu.getCat3Id()));
        categories.sort(Comparator.comparing(Category::getLevel));
        spuMaView.setCategory(CategoryView.withCategories(categories));
        return spuMaView;
    }
    private static ProductSpuMaView convert(ProductSpu productSpu, SpuExtraInfo spuExtraInfo) {
        ProductSpuMaView productView = new ProductSpuMaView();
        productView.setSpu(productSpu.getSpu());
        productView.setAreaId(productSpu.getAreaId());
        productView.setSupplierId(productSpu.getSupplierId());
        productView.setCat3Id(productSpu.getCat3Id());
        productView.setTitle(productSpu.getTitle());
        productView.setPoster(productSpu.getPoster());
        productView.setVideo(productSpu.getVideo());
        productView.setUnit(productSpu.getUnitId());
        productView.setState(productSpu.getState());
        if (null != spuExtraInfo) {
            // 审核消息
            productView.setCheckMsg(spuExtraInfo.getCheckMsg());
            productView.setIsShowOnly(spuExtraInfo.getIsShowOnly());
            productView.setIsPublic(spuExtraInfo.getIsPublic());
            productView.setDeptIds(spuExtraInfo.getDeptIds());
        }
        return productView;
    }
    @Override
    public List<ProductSpuMaView> returnValueHandler(List<ProductSpu> products) {
        List<ProductSpuMaView> productSpuMaViews = new ArrayList<>();
        if (ICollUtil.isEmpty(products)) {
            return productSpuMaViews;
        }
        // SPU 扩展数据
        List<String> spuIds = ICollUtil.process2List(products, ProductSpu::getSpu);
        List<SpuExtraInfo> spuExtraInfos = spuExtraInfoService.listByIds(spuIds);
        Map<String, SpuExtraInfo> extraInfoMap = IMapUtil.coll2Map(spuExtraInfos, SpuExtraInfo::getSpu);

        Map<String, List<Category>> categoryCache = new HashMap<>();

        for (ProductSpu product : products) {
            SpuExtraInfo spuExtraInfo = extraInfoMap.get(product.getSpu());
            ProductSpuMaView spuMaView = convert(product, spuExtraInfo);
            // 分类数据
            List<Category> categories = categoryCache.get(product.getCat3Id());
            if (null == categories) {
                categories = categoryService.listWithCache(List.of(product.getCat1Id(), product.getCat2Id(), product.getCat3Id()));
                categories.sort(Comparator.comparing(Category::getLevel));
                categoryCache.put(product.getCat3Id(), categories);
            }
            spuMaView.setCategory(CategoryView.withCategories(categories));
            // sku 列表
            List<ProductSku> productSkus = productSkuService.listBySpu(product.getSpu());
            List<ProductSkuMaView> skuMaViews = new ArrayList<>();
            productSkus.forEach(productSku -> {
                ProductSkuMaView skuMaView = new ProductSkuMaView();
                skuMaView.setSku(productSku.getSku());
                skuMaViews.add(skuMaView);
            });
            spuMaView.setProductSkus(skuMaViews);
            productSpuMaViews.add(spuMaView);
        }
        return productSpuMaViews;
    }

    @Override
    public void removeVerify(ProductSpu productSpu) {
        // sku 是否关联订单
        List<ProductSku> productSkus = productSkuService.listBySpu(productSpu.getSpu());
        List<String> skuNos = ICollUtil.process2List(productSkus, ProductSku::getSku);
        List<OrderDetail> orderDetails = orderDetailService.listBySkus(skuNos);
        if (ICollUtil.isNotEmpty(orderDetails)) {
            throw new ParamException(ProductResult.PRODUCT_UNSUPPORTED_DEL);
        }
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCT_SPU_CACHE, key = "#spu", unless = "#result==null")
    public ProductSpu findWithCache(String spu) {
        return this.getById(spu);
    }

    @Override
    public ProductSpu convertEntity(ProductSpuMaParam productSpuModel) {
        // 新增时,SPU 编码生成
        IParamUtil.setStrProperty(productSpuModel, ProductSpuMaParam::setSpu, productSpuModel.getSpu(), this::spuGenerate);
        // 分类数据
        Category category = categoryService.findWithCache(productSpuModel.getCat3Id());
        if (null == category) throw new ParamException(ProductResult.CAT_NOT_EXIST);
        ProductSpu productSpu = new ProductSpu();
        productSpu.setSpu(productSpuModel.getSpu());
        productSpu.setAreaId(productSpuModel.getAreaId());

        List<String> catIds = StrUtil.split(category.getFullPath(), IStrPool.CARET);
        productSpu.setCat1Id(catIds.getFirst());
        productSpu.setCat2Id(catIds.get(1));
        productSpu.setCat3Id(productSpuModel.getCat3Id());
        productSpu.setTitle(productSpuModel.getTitle());
        productSpu.setPoster(productSpuModel.getPoster());
        productSpu.setVideo(productSpuModel.getVideo());
        productSpu.setUnitId(productSpuModel.getUnit());
        productSpu.setCountSku(productSpuModel.getProductSkus().size());
        productSpu.setCountSale(0);
        // 前端参数空字符串进行 null 处理
        IParamUtil.setStrProperty(productSpu, ProductSpu::setVideo, productSpu.getVideo(), (String) null);
        // 默认商品为存稿状态
        productSpu.setState(ProductStateEnum.DRAFT.getCode());
        // if (productSpuModel.isAdd()) {
        //     // 税率
        //     productSpu.setTaxRate(IParamUtil.ofValue(productSpu.getTaxRate(), ProductConstants.TAX_RATE));
        // }
        return productSpu;
    }

    @Override
    public void upsertInit(ProductSpu productSpu) {
        // 三级分类信息
        Category category = categoryService.findWithCache(productSpu.getCat3Id());
        if (null == category) throw new ParamException(ProductResult.CAT_NOT_EXIST);
        // 仅仅允许三级分类编码下上传商品
        if (category.getLevel() != ProductConstants.LOW_CAT_LEVEL) {
            throw new ParamException(ProductResult.CAT_LEVEL_ILLEGAL);
        }

        String fullPath = category.getFullPath();
        List<String> catIds = StrUtil.split(fullPath, IStrPool.CARET);
        productSpu.setCat1Id(catIds.getFirst());
        productSpu.setCat2Id(catIds.get(1));
    }

    @Override
    public void uniqueCheck(ProductSpu productSpu) {

    }

    private String spuGenerate() {
        // Redis 自增ID
        return ProductConstants.SPU_PREFIX + XSequenceUtil.incrementNo(ProductConstants.SPU_NO_INCREMENT_KEY);
    }

}
