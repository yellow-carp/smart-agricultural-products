package com.xtechcn.cloud.customer.controller;


import com.xtechcn.cloud.customer.constants.CustomerResult;
import com.xtechcn.cloud.customer.constants.SupplierStateEnum;
import com.xtechcn.cloud.customer.entity.Supplier;
import com.xtechcn.cloud.customer.model.po.SupplierInfoQuery;
import com.xtechcn.cloud.customer.model.po.SupplierParam;
import com.xtechcn.cloud.customer.service.SupplierService;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.common.security.annotation.OpenApi;
import com.xtechcn.common.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 供应商管理
*
* @author hanjie
* @since 2025-09-16 09:36:46
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier/portal")
@Tag(description = "supplier-portal", name = "小程序-供应商")
public class SupplierPortalController {

    private final  SupplierService supplierService;

    @OpenApi
    @Transactional
    @SysLog("供应商申请")
    @PostMapping(value = "/apply")
    @Operation(description = "供应商申请", summary = "供应商申请")
    public R<Boolean> save(@RequestBody @Valid SupplierParam supplierParam) {
        // 状态验证
        if (null != supplierParam.getId()) {
            Supplier supplier = supplierService.getById(supplierParam.getId());
            if (null != supplier && supplier.getState() != SupplierStateEnum.EDITING.getCode() && supplier.getState() != SupplierStateEnum.REFUSE.getCode()) {
                return R.failed(CustomerResult.STATE_UNABLE_OPERATE);
            }
        }
        // 参数校验
        supplierService.check(supplierParam);
        // 参数转换
        Supplier supplier = supplierService.conventEntity(supplierParam);
        // 数据唯一性验证
        supplierService.uniqueCheck(supplier);
        // 数据初始化
        String userId = SecurityUtil.userId();
        supplierService.upsertInit(supplier, userId, SupplierStateEnum.AUDIT.getCode());
        // 数据入库
        boolean isOk = supplierParam.isAdd() ? supplierService.save(supplier) : supplierService.updateById(supplier);
        return R.ok(isOk);
    }


    @OpenApi
    @GetMapping(value = "/audit/state")
    @Operation(description = "查询供应商审核状态", summary = "查询供应商审核状态")
    public R<Supplier> getSupplier(@Valid SupplierInfoQuery supplierInfoQuery) {
        Supplier supplier = supplierService.infoQuery(supplierInfoQuery);
        return R.ok(supplier);
    }

}