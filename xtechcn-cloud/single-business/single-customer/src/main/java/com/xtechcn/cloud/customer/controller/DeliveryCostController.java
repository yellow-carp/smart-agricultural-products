package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.model.po.DeliveryCostParam;
import com.xtechcn.cloud.customer.model.vo.CarPageView;
import com.xtechcn.cloud.customer.model.vo.DeliveryCostView;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.DeliveryCost;
import com.xtechcn.cloud.customer.service.DeliveryCostService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 物流成本
*
* @author hanjie
* @since 2025-09-11 17:59:27
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/deliverycost")
@Tag(description = "deliverycost", name = "物流成本")
public class DeliveryCostController {

    private final  DeliveryCostService deliveryCostService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:deliverycost:r')")
    public R<Page<DeliveryCostView>> pageQuery(Page page, DeliveryCost deliveryCost) {
        page = deliveryCostService.page(page, Wrappers.lambdaQuery(deliveryCost));
        List<DeliveryCost> deliveryCosts = page.getRecords();
        if (ICollUtil.isNotEmpty(deliveryCosts)) {
            // 封装数据
            List<DeliveryCostView> deliveryCostViews = deliveryCostService.returnViewHandler(deliveryCosts);
            page.setRecords(deliveryCostViews);
        }
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('ct:deliverycost:r')")
    public R getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        DeliveryCost deliveryCost = deliveryCostService.getById(id);
        return R.ok(deliveryCost);
    }


    @SysLog("新增物流成本")
    @PostMapping(value = "/upsert")
    @Operation(description = "新增物流成本", summary = "新增物流成本")
    @PreAuthorize("@pmh.hasPermission('ct:deliverycost:w')")
    public R save(@RequestBody @Valid DeliveryCostParam deliveryCostParam) {
        // 参数转实体
        DeliveryCost deliveryCost = deliveryCostService.conventEntity(deliveryCostParam);
        // 判断该产区或销区是否存在
        deliveryCostService.exist(deliveryCost);
        // 重复验证
        deliveryCostService.uniqueCheck(deliveryCost);
        boolean isOk = deliveryCostParam.isAdd() ? deliveryCostService.save(deliveryCost) : deliveryCostService.updateById(deliveryCost);
        return R.ok(isOk);
    }

    @SysLog("通过id删除物流成本")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('ct:deliverycost:d')")
    @Operation(description = "指定id删除物流成本", summary = "指定id删除物流成本")
    public R removeById(@PathVariable String id) {
        boolean isOk = deliveryCostService.removeById(id);
        // 删除缓存
        deliveryCostService.removeCache(id);
        return R.ok(isOk);
    }

}