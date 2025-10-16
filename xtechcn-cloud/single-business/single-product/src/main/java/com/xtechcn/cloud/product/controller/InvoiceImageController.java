package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.InvoiceImage;
import com.xtechcn.cloud.product.service.InvoiceImageService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 发票信息
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/invoiceimage")
@Tag(description = "invoiceimage", name = "发票信息")
public class InvoiceImageController {

    private final  InvoiceImageService invoiceImageService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:invoiceimage:r')")
    public R pageQuery(Page page, InvoiceImage invoiceImage) {
        page = invoiceImageService.page(page, Wrappers.lambdaQuery(invoiceImage));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:invoiceimage:r')")
    public R getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        InvoiceImage invoiceImage = invoiceImageService.getById(id);
        return R.ok(invoiceImage);
    }


    @SysLog("新增发票信息")
    @PostMapping(value = "/add")
    @Operation(description = "新增发票信息", summary = "新增发票信息")
    @PreAuthorize("@pmh.hasPermission('pd:invoiceimage:w')")
    public R save(@RequestBody InvoiceImage invoiceImage) {
        boolean isOk = invoiceImageService.save(invoiceImage);
        return R.ok(isOk);
    }


    @SysLog("修改发票信息")
    @PutMapping(value = "/update")
    @Operation(description = "修改发票信息", summary = "修改发票信息")
    @PreAuthorize("@pmh.hasPermission('pd:invoiceimage:w')")
    public R updateById(@RequestBody InvoiceImage invoiceImage) {
        boolean isOk = invoiceImageService.updateById(invoiceImage);
        return R.ok(isOk);
    }


    @SysLog("通过id删除发票信息")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('pd:invoiceimage:d')")
    @Operation(description = "指定id删除发票信息", summary = "指定id删除发票信息")
    public R removeById(@PathVariable String id) {
        boolean isOk = invoiceImageService.removeById(id);
        return R.ok(isOk);
    }

}