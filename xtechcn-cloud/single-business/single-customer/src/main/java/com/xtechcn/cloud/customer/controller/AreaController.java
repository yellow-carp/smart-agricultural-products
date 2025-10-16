package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.constants.AreaTypeEnum;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.entity.ReceiveConfig;
import com.xtechcn.cloud.customer.entity.Unit;
import com.xtechcn.cloud.customer.model.po.AreaParam;
import com.xtechcn.cloud.customer.model.vo.AreaView;
import com.xtechcn.cloud.customer.model.vo.UnitView;
import com.xtechcn.cloud.customer.service.ReceiveConfigService;
import com.xtechcn.common.core.lang.Disenable;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.service.AreaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 区域管理
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/area")
@Tag(description = "area", name = "区域管理")
public class AreaController {

    private final AreaService areaService;

    private final ReceiveConfigService receiveConfigService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:area:r')")
    public R<Page<AreaView>> pageQuery(Page page, Area area) {
        page = areaService.page(page, Wrappers.lambdaQuery(area));
        if (ICollUtil.isNotEmpty(page.getRecords())) {
            List<Area> areaList = page.getRecords();
            // 封装数据返回
            List<AreaView> areaViewList = areaService.returnViewHandler(areaList);
            page.setRecords(areaViewList);
        }
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('ct:area:r')")
    public R<Area> getById(@PathVariable("id") String id) {
        Area area = areaService.findAndCache(id);
        return R.ok(area);
    }


    @SysLog("新增修改区域管理")
    @PostMapping(value = "/upsert")
    @Operation(description = "修改区域管理", summary = "修改区域管理")
    @PreAuthorize("@pmh.hasPermission('ct:area:w')")
    public R updateById(@RequestBody @Valid AreaParam areaParam) {
        // 参数转实体
        Area area = areaService.conventEntity(areaParam);
        // 唯一性校验
        areaService.uniqueCheck(area);
        boolean isOk = areaParam.isAdd() ? areaService.save(area) : areaService.updateById(area);
        if (isOk) {
            // 添加数据插入到自动收货配置表
            receiveConfigService.init(area);
        }
        return R.ok(isOk);
    }

    @PostMapping(value = "/disenable")
    @Operation(description = "启停区域", summary = "启停区域")
    @PreAuthorize("@pmh.hasPermission('ct:area:w')")
    public R<Boolean> disenable(@RequestBody Disenable disenable) {
        Area area = areaService.findAndCache(disenable.ofId());
        if (null == area) return R.failed(FailedResult.NOT_FOUNT);
        if (Objects.equals(area.getEnabled(), disenable.enabled())) {
            return R.ok(true);
        }
        boolean isOk = areaService.updateById(area.withId().enable(disenable.enabled()));
        return R.ok(isOk);
    }


    @SysLog("通过id删除区域管理")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('ct:area:d')")
    @Transactional
    @Operation(description = "指定id删除区域管理", summary = "指定id删除区域管理")
    public R removeById(@PathVariable String id) {
        Area area = areaService.findAndCache(id);
        if (area == null) return R.ok(true);
        boolean isOk = areaService.removeById(id);
        // 删除与该区域关联的自动收货配置数据
        Integer type = area.getType();
        if (type == AreaTypeEnum.AREA_ORIGIN.getCode()) {
            receiveConfigService.remove(Wrappers.<ReceiveConfig>lambdaQuery().eq(ReceiveConfig::getAreaOriginId, id));
        } else {
            receiveConfigService.remove(Wrappers.<ReceiveConfig>lambdaQuery().eq(ReceiveConfig::getAreaSalesId, id));
        }
        areaService.removeCache(id);
        return R.ok(isOk);
    }

}