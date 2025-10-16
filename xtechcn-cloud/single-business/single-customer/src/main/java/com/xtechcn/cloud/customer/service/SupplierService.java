package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.Supplier;
import com.xtechcn.cloud.customer.model.po.SupplierInfoQuery;
import com.xtechcn.cloud.customer.model.po.SupplierParam;
import jakarta.validation.Valid;

/**
* 供应商管理
*
* @author hanjie
* @since 2025-09-16 09:36:46
*/
public interface SupplierService extends IService<Supplier> {

    /**
     * 参数转实体
     */
    Supplier conventEntity(SupplierParam supplierParam);

    /**
     * 唯一性校验
     */
    void uniqueCheck(Supplier supplier);

    /**
     * 新增/修改初始化
     */
    void upsertInit(Supplier supplier, String userId, Integer state);

    /**
     * 查询供应商详情
     */
    Supplier infoQuery(SupplierInfoQuery supplierInfoQuery);

    /**
     * 参数校验
     */
    void check(SupplierParam supplierParam);

    /**
     *  注册
     */
    void register(Supplier supplier);
}