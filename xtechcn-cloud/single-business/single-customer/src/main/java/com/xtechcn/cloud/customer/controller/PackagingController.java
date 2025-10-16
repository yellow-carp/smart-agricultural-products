package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.model.po.PackagingParam;
import com.xtechcn.cloud.customer.model.vo.PackagingView;
import com.xtechcn.common.core.lang.Disenable;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.Packaging;
import com.xtechcn.cloud.customer.service.PackagingService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 包装形式
 *
 * @author hanjie
 * @since 2025-09-11 17:59:28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/packaging")
@Tag(description = "packaging", name = "包装形式")
public class PackagingController {

    private final PackagingService packagingService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:packaging:r')")
    public R<Page<PackagingView>> pageQuery(Page page, Packaging packaging) {
        page = packagingService.page(page, Wrappers.lambdaQuery(packaging));
        if(ICollUtil.isNotEmpty(page.getRecords())){
            List<Packaging> packagingList = page.getRecords();
            // 封装数据并返回
            List<PackagingView> packagingViewList = packagingService.returnViewHandler(packagingList);
            page.setRecords(packagingViewList);
        }
        return R.ok(page);
    }

    @PostMapping(value = "/disenable")
    @Operation(description = "启停包装", summary = "启停包装")
    @PreAuthorize("@pmh.hasPermission('ct:packaging:w')")
    public R<Boolean> disenable(@RequestBody Disenable disenable) {
        Packaging packaging = packagingService.findAndCache(disenable.ofId());
        if (null ==  packaging) return R.failed(FailedResult.NOT_FOUNT);
        if (Objects.equals(packaging.getEnabled(), disenable.enabled())){
            return R.ok(true);
        }
        boolean isOk = packagingService.updateById(packaging.withId().enable(disenable.enabled()));
        return R.ok(isOk);
    }
    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('ct:packaging:r')")
    public R<Packaging> getById(@PathVariable("id") Long id) {
        // 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        Packaging packaging = packagingService.getById(id);
        return R.ok(packaging);
    }


    @SysLog("新增包装形式")
    @PostMapping(value = "/upsert")
    @Operation(description = "新增包装形式", summary = "新增包装形式")
    @PreAuthorize("@pmh.hasPermission('ct:packaging:w')")
    public R save(@RequestBody @Valid PackagingParam packagingParam) {
        // 参数转实体
        Packaging packaging = packagingService.conventEntity(packagingParam);
        // 校验唯一性
        packagingService.uniqueCheck(packaging);
        boolean isOk = packagingParam.isAdd() ? packagingService.save(packaging) : packagingService.updateById(packaging);
        return R.ok(isOk);
    }


    @SysLog("通过id删除包装形式")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('ct:packaging:d')")
    @Operation(description = "指定id删除包装形式", summary = "指定id删除包装形式")
    public R removeById(@PathVariable Long id) {
        Packaging packaging = packagingService.getById(id);
        if (packaging == null) return R.ok(true);
        boolean isOk = packagingService.removeById(id);
        return R.ok(isOk);
    }

}