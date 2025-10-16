package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.model.po.ReceiveConfigParam;
import com.xtechcn.cloud.customer.model.vo.AreaView;
import com.xtechcn.cloud.customer.model.vo.ReceiveConfigView;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.ReceiveConfig;
import com.xtechcn.cloud.customer.service.ReceiveConfigService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 自动收货配置
*
* @author hanjie
* @since 2025-09-11 17:59:28
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/receive/config")
@Tag(description = "receiveconfig", name = "自动收货配置")
public class ReceiveConfigController {

    private final  ReceiveConfigService receiveConfigService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:receiveconfig:r')")
    public R<Page<ReceiveConfigView>> pageQuery(Page page, ReceiveConfig receiveConfig) {
        page = receiveConfigService.page(page, Wrappers.lambdaQuery(receiveConfig));
        List<ReceiveConfig> receiveConfigList = page.getRecords();
        if(ICollUtil.isNotEmpty(receiveConfigList)){
            // 封装数据返回
            List<ReceiveConfigView> receiveConfigViews = receiveConfigService.returnViewHandler(receiveConfigList);
            page.setRecords(receiveConfigViews);
        }
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('ct:receiveconfig:r')")
    public R<ReceiveConfig> getById(@PathVariable("id") String id) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        ReceiveConfig receiveConfig = receiveConfigService.findAndCache(id);
        return R.ok(receiveConfig);
    }


    @SysLog("新增修改自动收货配置")
    @PostMapping(value = "/upsert")
    @Operation(description = "新增修改自动收货配置", summary = "新增自动收货配置")
    @PreAuthorize("@pmh.hasPermission('ct:receiveconfig:w')")
    public R save(@RequestBody @Valid ReceiveConfigParam receiveConfigParam) {
        // 参数转实体
        ReceiveConfig receiveConfig = receiveConfigService.conventEntity(receiveConfigParam);
        // 重复性验证
        receiveConfigService.uniqueCheck(receiveConfig);
        boolean isOk = receiveConfigParam.isAdd() ? receiveConfigService.save(receiveConfig) : receiveConfigService.updateById(receiveConfig);
        return R.ok(isOk);
    }


    @SysLog("通过id删除自动收货配置")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('ct:receiveconfig:d')")
    @Operation(description = "指定id删除自动收货配置", summary = "指定id删除自动收货配置")
    public R removeById(@PathVariable String id) {
        boolean isOk = receiveConfigService.removeById(id);
        // 删除缓存
        receiveConfigService.removeCache(id);
        return R.ok(isOk);
    }

}