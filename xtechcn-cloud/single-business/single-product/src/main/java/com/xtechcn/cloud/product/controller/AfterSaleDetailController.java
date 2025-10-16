package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.AfterSaleDetail;
import com.xtechcn.cloud.product.service.AfterSaleDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 售后详情表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/aftersaledetail")
@Tag(description = "aftersaledetail", name = "售后详情表")
public class AfterSaleDetailController {

    private final  AfterSaleDetailService afterSaleDetailService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:aftersaledetail:r')")
    public R pageQuery(Page page, AfterSaleDetail afterSaleDetail) {
        page = afterSaleDetailService.page(page, Wrappers.lambdaQuery(afterSaleDetail));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:aftersaledetail:r')")
    public R getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        AfterSaleDetail afterSaleDetail = afterSaleDetailService.getById(id);
        return R.ok(afterSaleDetail);
    }


    @SysLog("新增售后详情表")
    @PostMapping(value = "/add")
    @Operation(description = "新增售后详情表", summary = "新增售后详情表")
    @PreAuthorize("@pmh.hasPermission('pd:aftersaledetail:w')")
    public R save(@RequestBody AfterSaleDetail afterSaleDetail) {
        boolean isOk = afterSaleDetailService.save(afterSaleDetail);
        return R.ok(isOk);
    }


    @SysLog("修改售后详情表")
    @PutMapping(value = "/update")
    @Operation(description = "修改售后详情表", summary = "修改售后详情表")
    @PreAuthorize("@pmh.hasPermission('pd:aftersaledetail:w')")
    public R updateById(@RequestBody AfterSaleDetail afterSaleDetail) {
        boolean isOk = afterSaleDetailService.updateById(afterSaleDetail);
        return R.ok(isOk);
    }


    @SysLog("通过id删除售后详情表")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('pd:aftersaledetail:d')")
    @Operation(description = "指定id删除售后详情表", summary = "指定id删除售后详情表")
    public R removeById(@PathVariable String id) {
        boolean isOk = afterSaleDetailService.removeById(id);
        return R.ok(isOk);
    }

}