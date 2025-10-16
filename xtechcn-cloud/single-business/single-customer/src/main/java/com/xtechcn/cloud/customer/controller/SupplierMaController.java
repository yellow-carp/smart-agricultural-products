package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.constants.CustomerResult;
import com.xtechcn.cloud.customer.constants.SupplierStateEnum;
import com.xtechcn.cloud.customer.model.po.AuditParam;
import com.xtechcn.cloud.customer.model.po.SupplierParam;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.lang.Disenable;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.Supplier;
import com.xtechcn.cloud.customer.service.SupplierService;
import com.xtechcn.common.security.utils.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 供应商管理
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier/ma")
@Tag(description = "supplier-ma", name = "供应商管理")
public class SupplierMaController {

    private final SupplierService supplierService;

    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    public R<Page<Supplier>> pageQuery(Page page, Supplier supplier) {
        page = supplierService.page(page, Wrappers.lambdaQuery(supplier).orderByDesc(Supplier::getCreateTime));
        return R.ok(page);
    }

    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('ct:supplier:r')")
    public R<Supplier> getById(@PathVariable("id") String id) {
        Supplier supplier = supplierService.getById(id);
        return R.ok(supplier);
    }

    @SysLog("新增/修改供应商")
    @PostMapping(value = "/upsert")
    @Operation(description = "新增/修改供应商", summary = "新增/修改供应商")
    @PreAuthorize("@pmh.hasPermission('ct:supplier:w')")
    public R upsert(@RequestBody SupplierParam supplierParam) {
        if (Objects.isNull(supplierParam.getUserId())) {
            throw new ParamException(CustomerResult.USERID_NOT_NULL);
        }
        // 参数转换
        Supplier Supplier = supplierService.conventEntity(supplierParam);
        // 数据唯一性验证
        supplierService.uniqueCheck(Supplier);
        // 数据初始化
        supplierService.upsertInit(Supplier, supplierParam.getUserId(), SupplierStateEnum.PASS.getCode());
        // 数据入库
        boolean isOk = supplierParam.isAdd() ? supplierService.save(Supplier) : supplierService.updateById(Supplier);
        return R.ok(isOk);
    }

    @SysLog("审核供应商")
    @PostMapping(value = "/audit")
    @Operation(description = "审核供应商", summary = "审核供应商")
    @PreAuthorize("@pmh.hasPermission('ct:supplier:w')")
    public R audit(@RequestBody AuditParam auditParam) {
        Supplier supplier = supplierService.getById(auditParam.getId());
        if (null == supplier) return R.failed(FailedResult.NOT_FOUNT);
        if (!supplier.getState().equals(SupplierStateEnum.AUDIT.getCode())) {
            return R.failed(CustomerResult.STATE_UNABLE_OPERATE);
        }
        Supplier newSupplier = supplier.withId()
                .checkMsg(auditParam.getPassed() ? "" : auditParam.getAuditMsg())
                .state(auditParam.getPassed() ? SupplierStateEnum.PASS.getCode() : SupplierStateEnum.REFUSE.getCode());
        boolean isOk = supplierService.updateById(newSupplier);
        if (isOk) {
            // 注册用户账号
            supplierService.register(supplier);
        }
        return R.ok(isOk);
    }

    @PostMapping(value = "/disenable")
    @Operation(description = "启停供应商", summary = "启停供应商")
    @PreAuthorize("@pmh.hasPermission('ct:supplier:w')")
    public R<Boolean> disenable(@RequestBody Disenable disenable) {
        Supplier supplier = supplierService.getById(disenable.ofId());
        if (null == supplier) return R.failed(FailedResult.NOT_FOUNT);
        if (Objects.equals(supplier.getEnabled(), disenable.enabled())) {
            return R.ok(true);
        }
        boolean isOk = supplierService.updateById(supplier.withId().enabled(disenable.enabled()));
        return R.ok(isOk);
    }
}