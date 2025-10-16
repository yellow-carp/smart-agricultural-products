package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.model.po.UnitPageParam;
import com.xtechcn.cloud.customer.model.po.UnitParam;
import com.xtechcn.cloud.customer.model.vo.UnitView;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.Unit;
import com.xtechcn.cloud.customer.service.UnitService;
import com.xtechcn.common.mp.wrappers.IWrappers;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品单位
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/unit")
@Tag(description = "unit", name = "商品单位")
public class UnitController {

    private final UnitService unitService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:unit:r')")
    public R<Page<UnitView>> pageQuery(Page page, UnitPageParam unitPageParam) {
        page = unitService.page(page, IWrappers.<Unit>lambdaQuery(unitPageParam));
        if (ICollUtil.isNotEmpty(page.getRecords())) {
            List<Unit> unitList = page.getRecords();
            // 封装数据返回
            List<UnitView> unitViews = unitService.returnViewHandler(unitList);
            page.setRecords(unitViews);
        }
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('ct:unit:r')")
    public R<Unit> getById(@PathVariable("id") Long id) {

        // 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        Unit unit = unitService.getById(id);
        return R.ok(unit);
    }


    @SysLog("新增修改商品单位")
    @PostMapping(value = "/upsert")
    @Operation(description = "新增商品单位", summary = "新增商品单位")
    @PreAuthorize("@pmh.hasPermission('ct:unit:w')")
    public R save(@RequestBody @Valid UnitParam unitParam) {
        // 参数转实体
        Unit unit = unitService.conventEntity(unitParam);
        // 校验唯一性
        unitService.uniqueCheck(unit);
        boolean isOk = unitParam.isAdd() ? unitService.save(unit) : unitService.updateById(unit);
        return R.ok(isOk);
    }



    @SysLog("通过id删除商品单位")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('ct:unit:d')")
    @Operation(description = "指定id删除商品单位", summary = "指定id删除商品单位")
    public R removeById(@PathVariable Long id) {
        Unit ut = unitService.getById(id);
        if (ut == null) return R.ok(true);
        boolean isOk = unitService.removeById(id);
        return R.ok(isOk);
    }

}