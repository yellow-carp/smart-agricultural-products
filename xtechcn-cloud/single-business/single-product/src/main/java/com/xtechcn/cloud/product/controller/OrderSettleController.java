package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.OrderSettle;
import com.xtechcn.cloud.product.service.OrderSettleService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 订单结算详情
*
* @author hanjie
* @since 2025-09-16 09:45:56
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/ordersettle")
@Tag(description = "ordersettle", name = "订单结算详情")
public class OrderSettleController {

    private final  OrderSettleService orderSettleService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:ordersettle:r')")
    public R pageQuery(Page page, OrderSettle orderSettle) {
        page = orderSettleService.page(page, Wrappers.lambdaQuery(orderSettle));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{orderNo}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:ordersettle:r')")
    public R getById(@PathVariable("orderNo") String orderNo) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        OrderSettle orderSettle = orderSettleService.getById(orderNo);
        return R.ok(orderSettle);
    }


    @SysLog("新增订单结算详情")
    @PostMapping(value = "/add")
    @Operation(description = "新增订单结算详情", summary = "新增订单结算详情")
    @PreAuthorize("@pmh.hasPermission('pd:ordersettle:w')")
    public R save(@RequestBody OrderSettle orderSettle) {
        boolean isOk = orderSettleService.save(orderSettle);
        return R.ok(isOk);
    }


    @SysLog("修改订单结算详情")
    @PutMapping(value = "/update")
    @Operation(description = "修改订单结算详情", summary = "修改订单结算详情")
    @PreAuthorize("@pmh.hasPermission('pd:ordersettle:w')")
    public R updateById(@RequestBody OrderSettle orderSettle) {
        boolean isOk = orderSettleService.updateById(orderSettle);
        return R.ok(isOk);
    }


    @SysLog("通过id删除订单结算详情")
    @DeleteMapping(value = "/remove/{orderNo}")
    @PreAuthorize("@pmh.hasPermission('pd:ordersettle:d')")
    @Operation(description = "指定id删除订单结算详情", summary = "指定id删除订单结算详情")
    public R removeById(@PathVariable String orderNo) {
        boolean isOk = orderSettleService.removeById(orderNo);
        return R.ok(isOk);
    }

}