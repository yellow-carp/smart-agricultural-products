package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.PurchaserConfig;
import com.xtechcn.cloud.customer.model.po.PurchaserConfigParam;
import com.xtechcn.cloud.customer.model.vo.PurchaserConfigView;
import jakarta.validation.Valid;

import java.util.List;

/**
* 采购商入驻配置
*
* @author hanjie
* @since 2025-09-16 09:36:46
*/
public interface PurchaserConfigService extends IService<PurchaserConfig> {

    /**
     */
    PurchaserConfig getConfig();

    PurchaserConfigView returnViewHandler(PurchaserConfig purchaserConfig);

    PurchaserConfig conventEntity(@Valid PurchaserConfigParam purchaserConfigParam);
}