package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.EvaluationSupplier;
import com.xtechcn.cloud.customer.service.EvaluationSupplierService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 评价管理
*
* @author hanjie
* @since 2025-09-16 09:36:46
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/evaluationsupplier")
@Tag(description = "evaluationsupplier", name = "评价管理")
public class EvaluationSupplierController {

    private final  EvaluationSupplierService evaluationSupplierService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:evaluationsupplier:r')")
    public R pageQuery(Page page, EvaluationSupplier evaluationSupplier) {
        page = evaluationSupplierService.page(page, Wrappers.lambdaQuery(evaluationSupplier));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('ct:evaluationsupplier:r')")
    public R getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        EvaluationSupplier evaluationSupplier = evaluationSupplierService.getById(id);
        return R.ok(evaluationSupplier);
    }


    @SysLog("新增评价管理")
    @PostMapping(value = "/add")
    @Operation(description = "新增评价管理", summary = "新增评价管理")
    @PreAuthorize("@pmh.hasPermission('ct:evaluationsupplier:w')")
    public R save(@RequestBody EvaluationSupplier evaluationSupplier) {
        boolean isOk = evaluationSupplierService.save(evaluationSupplier);
        return R.ok(isOk);
    }


    @SysLog("修改评价管理")
    @PutMapping(value = "/update")
    @Operation(description = "修改评价管理", summary = "修改评价管理")
    @PreAuthorize("@pmh.hasPermission('ct:evaluationsupplier:w')")
    public R updateById(@RequestBody EvaluationSupplier evaluationSupplier) {
        boolean isOk = evaluationSupplierService.updateById(evaluationSupplier);
        return R.ok(isOk);
    }


    @SysLog("通过id删除评价管理")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('ct:evaluationsupplier:d')")
    @Operation(description = "指定id删除评价管理", summary = "指定id删除评价管理")
    public R removeById(@PathVariable String id) {
        boolean isOk = evaluationSupplierService.removeById(id);
        return R.ok(isOk);
    }

}