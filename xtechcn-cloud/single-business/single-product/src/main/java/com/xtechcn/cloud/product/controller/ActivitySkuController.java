package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.ActivitySku;
import com.xtechcn.cloud.product.service.ActivitySkuService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 特价商品管理
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/activitysku")
@Tag(description = "activitysku", name = "特价商品管理")
public class ActivitySkuController {

    private final  ActivitySkuService activitySkuService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:activitysku:r')")
    public R pageQuery(Page page, ActivitySku activitySku) {
        page = activitySkuService.page(page, Wrappers.lambdaQuery(activitySku));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:activitysku:r')")
    public R getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        ActivitySku activitySku = activitySkuService.getById(id);
        return R.ok(activitySku);
    }


    @SysLog("新增特价商品管理")
    @PostMapping(value = "/add")
    @Operation(description = "新增特价商品管理", summary = "新增特价商品管理")
    @PreAuthorize("@pmh.hasPermission('pd:activitysku:w')")
    public R save(@RequestBody ActivitySku activitySku) {
        boolean isOk = activitySkuService.save(activitySku);
        return R.ok(isOk);
    }


    @SysLog("修改特价商品管理")
    @PutMapping(value = "/update")
    @Operation(description = "修改特价商品管理", summary = "修改特价商品管理")
    @PreAuthorize("@pmh.hasPermission('pd:activitysku:w')")
    public R updateById(@RequestBody ActivitySku activitySku) {
        boolean isOk = activitySkuService.updateById(activitySku);
        return R.ok(isOk);
    }


    @SysLog("通过id删除特价商品管理")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('pd:activitysku:d')")
    @Operation(description = "指定id删除特价商品管理", summary = "指定id删除特价商品管理")
    public R removeById(@PathVariable String id) {
        boolean isOk = activitySkuService.removeById(id);
        return R.ok(isOk);
    }

}