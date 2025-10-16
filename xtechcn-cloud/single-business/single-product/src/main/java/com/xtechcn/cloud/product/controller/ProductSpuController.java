package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.ProductSpu;
import com.xtechcn.cloud.product.service.ProductSpuService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 商品SPU表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/productspu")
@Tag(description = "productspu", name = "商品SPU表")
public class ProductSpuController {

    private final  ProductSpuService productSpuService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:productspu:r')")
    public R pageQuery(Page page, ProductSpu productSpu) {
        page = productSpuService.page(page, Wrappers.lambdaQuery(productSpu));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{spu}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:productspu:r')")
    public R getById(@PathVariable("spu") String spu) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        ProductSpu productSpu = productSpuService.getById(spu);
        return R.ok(productSpu);
    }


    @SysLog("新增商品SPU表")
    @PostMapping(value = "/add")
    @Operation(description = "新增商品SPU表", summary = "新增商品SPU表")
    @PreAuthorize("@pmh.hasPermission('pd:productspu:w')")
    public R save(@RequestBody ProductSpu productSpu) {
        boolean isOk = productSpuService.save(productSpu);
        return R.ok(isOk);
    }


    @SysLog("修改商品SPU表")
    @PutMapping(value = "/update")
    @Operation(description = "修改商品SPU表", summary = "修改商品SPU表")
    @PreAuthorize("@pmh.hasPermission('pd:productspu:w')")
    public R updateById(@RequestBody ProductSpu productSpu) {
        boolean isOk = productSpuService.updateById(productSpu);
        return R.ok(isOk);
    }


    @SysLog("通过id删除商品SPU表")
    @DeleteMapping(value = "/remove/{spu}")
    @PreAuthorize("@pmh.hasPermission('pd:productspu:d')")
    @Operation(description = "指定id删除商品SPU表", summary = "指定id删除商品SPU表")
    public R removeById(@PathVariable String spu) {
        boolean isOk = productSpuService.removeById(spu);
        return R.ok(isOk);
    }

}