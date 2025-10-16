package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.product.constants.ProductResult;
import com.xtechcn.cloud.product.constants.ProductStateEnum;
import com.xtechcn.cloud.product.entity.ProductQueryParam;
import com.xtechcn.cloud.product.entity.ProductSku;
import com.xtechcn.cloud.product.entity.ProductSpu;
import com.xtechcn.cloud.product.listener.ProductTakeUpEvent;
import com.xtechcn.cloud.product.model.ProductSpuModel;
import com.xtechcn.cloud.product.model.po.ProductSpuMaParam;
import com.xtechcn.cloud.product.model.vo.ProductSpuMaView;
import com.xtechcn.cloud.product.service.*;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.core.utils.IStrUtil;
import com.xtechcn.common.lock.annotation.XLock4j;
import com.xtechcn.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理
 *
 * @author Weijixiao
 * @since 2025-09-17 11:23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(description = "product", name = "商品管理--综合")
public class ProductController {

    private final ProductSpuService productSpuService;
    private final ProductSkuService productSkuService;
    private final SpuExtraInfoService spuExtraInfoService;
    private final Facade4ProductSpu facade4ProductSpu;
    private final Facade4ProductSku facade4ProductSku;
    private final ProductPushExecutor productPushExecutor;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping(value = "/page")
    @PreAuthorize("@pmh.hasPermission('goods:view')")
    @Operation(summary = "分页查询", description = "分页查询")
    public R pageQuery(Page page, ProductQueryParam queryParam) {
        // 为了方便用户查询，增加SKU 查询的功能
        String sku = queryParam.getSku();
        if (IStrUtil.isNotBlank(queryParam.getSku())) {
            ProductSku productSku = productSkuService.findWithCache(sku);
            if (null == productSku) {
                return R.failed(FailedResult.NOT_FOUNT);
            }
            queryParam.setSpuExact(productSku.getSpu());
        }
        // 分页查询数据
        page = productSpuService.pageQuery(page, queryParam);
        List<ProductSpu> products = page.getRecords();
        if (!products.isEmpty()) {
            List<ProductSpuMaView> productSpuMaViews = productSpuService.returnValueHandler(products);
            page.setRecords(productSpuMaViews);
        }
        return R.ok(page);
    }

    @GetMapping(value = "/details/{spu}")
    @PreAuthorize("@pmh.hasPermission('goods:view')")
    @Operation(summary = "商品SPU和SKU详情", description = "商品SPU和SKU详情")
    public R<ProductSpuModel> details(@PathVariable(value = "spu") String spu) {
        ProductSpu productSpu = productSpuService.findWithCache(spu);
        if (null == productSpu) return R.failed(FailedResult.NOT_FOUNT);
        // 商品属性数据返回视图
        ProductSpuMaView productMaView = productSpuService.returnValueHandler(productSpu);
        // 返回数据处理
        spuExtraInfoService.fillDetailModels(productMaView);
        // spu 规格参数处理
        facade4ProductSpu.processSpecAttrModels(productMaView);
        // SPU 销售属性处理
        facade4ProductSpu.processSaleAttrModels(productMaView);
        // Sku 数据处理
        facade4ProductSku.processProductSkuModels(productMaView);
        // 存储数据处理
        // facade4ProductSku.processInventoryModels(productMaView.getProductSkus());

        return R.ok(productMaView);
    }

    @PostMapping(value = "/push")
    @SysLog(value = "商品综合信息上传")
    @XLock4j(keys = {"#productSpuModel.getSpu()"})
    @PreAuthorize("@pmh.hasPermission('goods:upsert')")
    @Operation(summary = "商品综合信息上传", description = "商品综合信息上传")
    public R push(@RequestBody @Valid ProductSpuMaParam productSpuModel) {
        // 参数前置处理
        facade4ProductSpu.preProcessHandler(productSpuModel);
        // 参数验证
        facade4ProductSpu.pushParameterVerify(productSpuModel);
        // 执行商品上传
        productSpuModel = productPushExecutor.executePush(productSpuModel);
        // 运营权限的,直接自动上架
        if (productSpuModel.getAutoUp()) {
            applicationEventPublisher.publishEvent(ProductTakeUpEvent.of(productSpuModel.getSpu()));
        }
        return R.ok(productSpuModel.getSpu());
    }

    @Transactional
    @SysLog(value = "删除SPU")
    @DeleteMapping(value = "/remove/{spu}")
    @PreAuthorize("@pmh.hasPermission('goods:del')")
    @Operation(summary = "删除SPU", description = "删除SPU")
    public R remove(@PathVariable("spu") String spu) {
        ProductSpu productSpu = productSpuService.findWithCache(spu);
        if (null == productSpu) return R.failed(FailedResult.NOT_FOUNT);

        productSpuService.removeVerify(productSpu);

        ProductStateEnum state = ProductStateEnum.codeOf(productSpu.getState());
        if (state.ge(ProductStateEnum.DOWN)) {
            // 商品只有下架状态及之下的状态才允许删除
            return R.failed(ProductResult.PRODUCT_STATE_ILLEGAL);
        }


        // 删除商品SPU 相关关联表数据
        productSpuService.removeById(spu);
        facade4ProductSpu.removeAboutSpu(spu);

        // 删除商品SPU 相关关联表数据
        List<ProductSku> productSkus = productSkuService.listBySpu(spu);
        List<String> skuNos = ICollUtil.process2List(productSkus, ProductSku::getSku);
        productSkuService.removeBatchByIds(skuNos);
        facade4ProductSku.removeAboutSkus(spu, skuNos);

        return R.ok();
    }


}
