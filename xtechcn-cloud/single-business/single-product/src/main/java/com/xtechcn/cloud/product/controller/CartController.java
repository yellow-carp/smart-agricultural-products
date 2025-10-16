package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.Cart;
import com.xtechcn.cloud.product.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 购物车表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Tag(description = "cart", name = "购物车表")
public class CartController {

    private final  CartService cartService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:cart:r')")
    public R pageQuery(Page page, Cart cart) {
        page = cartService.page(page, Wrappers.lambdaQuery(cart));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:cart:r')")
    public R getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        Cart cart = cartService.getById(id);
        return R.ok(cart);
    }


    @SysLog("新增购物车表")
    @PostMapping(value = "/add")
    @Operation(description = "新增购物车表", summary = "新增购物车表")
    @PreAuthorize("@pmh.hasPermission('pd:cart:w')")
    public R save(@RequestBody Cart cart) {
        boolean isOk = cartService.save(cart);
        return R.ok(isOk);
    }


    @SysLog("修改购物车表")
    @PutMapping(value = "/update")
    @Operation(description = "修改购物车表", summary = "修改购物车表")
    @PreAuthorize("@pmh.hasPermission('pd:cart:w')")
    public R updateById(@RequestBody Cart cart) {
        boolean isOk = cartService.updateById(cart);
        return R.ok(isOk);
    }


    @SysLog("通过id删除购物车表")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('pd:cart:d')")
    @Operation(description = "指定id删除购物车表", summary = "指定id删除购物车表")
    public R removeById(@PathVariable String id) {
        boolean isOk = cartService.removeById(id);
        return R.ok(isOk);
    }

}