package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.customer.entity.PurchaserConfig;
import com.xtechcn.cloud.customer.mapper.PurchaserConfigMapper;
import com.xtechcn.cloud.customer.model.po.PurchaserConfigParam;
import com.xtechcn.cloud.customer.model.vo.PurchaserConfigView;
import com.xtechcn.cloud.customer.service.PurchaserConfigService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
* 采购商入驻配置
*
* @author hanjie
* @since 2025-09-16 09:36:46
*/
@Service
public class PurchaserConfigServiceImpl extends ServiceImpl<PurchaserConfigMapper, PurchaserConfig> implements PurchaserConfigService {

    @Override
    public PurchaserConfig getConfig() {
        PurchaserConfig config = this.getOne(Wrappers.<PurchaserConfig>lambdaQuery().last("LIMIT 1"));
        return config;
    }

    @Override
    public PurchaserConfigView returnViewHandler(PurchaserConfig purchaserConfigs) {
        PurchaserConfigView purchaserConfigView = new PurchaserConfigView();
        purchaserConfigView.setId(purchaserConfigs.getId());
        purchaserConfigView.setRegisterAmount(purchaserConfigs.getRegisterAmount());
        purchaserConfigView.setInviteCode(purchaserConfigs.getInviteCode());
        return purchaserConfigView;
    }

    @Override
    public PurchaserConfig conventEntity(PurchaserConfigParam purchaserConfigParam) {
        PurchaserConfig purchaserConfig = new PurchaserConfig();
        purchaserConfig.setId(purchaserConfigParam.getId());
        purchaserConfig.setRegisterAmount(purchaserConfigParam.getRegisterAmount());
        purchaserConfig.setInviteCode(purchaserConfigParam.getInviteCode());
        return purchaserConfig;
    }
}
