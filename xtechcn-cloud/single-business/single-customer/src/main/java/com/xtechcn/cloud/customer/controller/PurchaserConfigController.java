package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.model.po.PurchaserConfigParam;
import com.xtechcn.cloud.customer.model.vo.PurchaserConfigView;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.PurchaserConfig;
import com.xtechcn.cloud.customer.service.PurchaserConfigService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购商入驻配置
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/purchaser/config")
@Tag(description = "purchaserconfig", name = "采购商入驻配置")
public class PurchaserConfigController {

    private final PurchaserConfigService purchaserConfigService;


    @GetMapping(value = "/details")
    @Operation(description = "查询", summary = "查询")
    @PreAuthorize("@pmh.hasPermission('ct:purchaserconfig:r')")
    public R<PurchaserConfigView> getById() {
        PurchaserConfig purchaserConfig = purchaserConfigService.getConfig();
        if (purchaserConfig==null) {
            // 初始化数据库
            purchaserConfig.setRegisterAmount(new MoneyPenny(30000));
            purchaserConfigService.save(purchaserConfig);
        }
        // 封装数据返回
        PurchaserConfigView purchaserConfigView = purchaserConfigService.returnViewHandler(purchaserConfig);
        return R.ok(purchaserConfigView);
    }


    @SysLog("修改采购商入驻配置")
    @PutMapping(value = "/update")
    @Operation(description = "修改采购商入驻配置", summary = "修改采购商入驻配置")
    @PreAuthorize("@pmh.hasPermission('ct:purchaserconfig:w')")
    public R updateById(@RequestBody @Valid PurchaserConfigParam purchaserConfigParam) {
        // 参数转实体
        PurchaserConfig purchaserConfig = purchaserConfigService.conventEntity(purchaserConfigParam);
        boolean isOk = purchaserConfigService.updateById(purchaserConfig);
        return R.ok(isOk);
    }


}