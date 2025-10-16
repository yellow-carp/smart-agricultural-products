package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.Shipment;
import com.xtechcn.cloud.product.service.ShipmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 发货单表
*
* @author hanjie
* @since 2025-09-16 09:45:54
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/shipment")
@Tag(description = "shipment", name = "发货单表")
public class ShipmentController {

    private final  ShipmentService shipmentService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:shipment:r')")
    public R pageQuery(Page page, Shipment shipment) {
        page = shipmentService.page(page, Wrappers.lambdaQuery(shipment));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:shipment:r')")
    public R getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        Shipment shipment = shipmentService.getById(id);
        return R.ok(shipment);
    }


    @SysLog("新增发货单表")
    @PostMapping(value = "/add")
    @Operation(description = "新增发货单表", summary = "新增发货单表")
    @PreAuthorize("@pmh.hasPermission('pd:shipment:w')")
    public R save(@RequestBody Shipment shipment) {
        boolean isOk = shipmentService.save(shipment);
        return R.ok(isOk);
    }


    @SysLog("修改发货单表")
    @PutMapping(value = "/update")
    @Operation(description = "修改发货单表", summary = "修改发货单表")
    @PreAuthorize("@pmh.hasPermission('pd:shipment:w')")
    public R updateById(@RequestBody Shipment shipment) {
        boolean isOk = shipmentService.updateById(shipment);
        return R.ok(isOk);
    }


    @SysLog("通过id删除发货单表")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('pd:shipment:d')")
    @Operation(description = "指定id删除发货单表", summary = "指定id删除发货单表")
    public R removeById(@PathVariable String id) {
        boolean isOk = shipmentService.removeById(id);
        return R.ok(isOk);
    }

}