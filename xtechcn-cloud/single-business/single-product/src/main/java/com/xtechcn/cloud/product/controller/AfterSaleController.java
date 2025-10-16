package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.AfterSale;
import com.xtechcn.cloud.product.service.AfterSaleService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 售后表
*
* @author hanjie
* @since 2025-09-16 09:45:54
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/aftersale")
@Tag(description = "aftersale", name = "售后表")
public class AfterSaleController {

    private final  AfterSaleService afterSaleService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:aftersale:r')")
    public R pageQuery(Page page, AfterSale afterSale) {
        page = afterSaleService.page(page, Wrappers.lambdaQuery(afterSale));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:aftersale:r')")
    public R getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        AfterSale afterSale = afterSaleService.getById(id);
        return R.ok(afterSale);
    }


    @SysLog("新增售后表")
    @PostMapping(value = "/add")
    @Operation(description = "新增售后表", summary = "新增售后表")
    @PreAuthorize("@pmh.hasPermission('pd:aftersale:w')")
    public R save(@RequestBody AfterSale afterSale) {
        boolean isOk = afterSaleService.save(afterSale);
        return R.ok(isOk);
    }


    @SysLog("修改售后表")
    @PutMapping(value = "/update")
    @Operation(description = "修改售后表", summary = "修改售后表")
    @PreAuthorize("@pmh.hasPermission('pd:aftersale:w')")
    public R updateById(@RequestBody AfterSale afterSale) {
        boolean isOk = afterSaleService.updateById(afterSale);
        return R.ok(isOk);
    }


    @SysLog("通过id删除售后表")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('pd:aftersale:d')")
    @Operation(description = "指定id删除售后表", summary = "指定id删除售后表")
    public R removeById(@PathVariable String id) {
        boolean isOk = afterSaleService.removeById(id);
        return R.ok(isOk);
    }

}