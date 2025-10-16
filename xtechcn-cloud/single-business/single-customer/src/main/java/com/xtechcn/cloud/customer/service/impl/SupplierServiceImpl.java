package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.api.login.BaseUserEntity;
import com.xtechcn.cloud.api.system.RemoteUserService;
import com.xtechcn.cloud.api.system.model.AddAccountModel;
import com.xtechcn.cloud.api.system.model.UserProfile;
import com.xtechcn.cloud.customer.constants.CustomerConstants;
import com.xtechcn.cloud.customer.constants.CustomerResult;
import com.xtechcn.cloud.customer.entity.Supplier;
import com.xtechcn.cloud.customer.mapper.SupplierMapper;
import com.xtechcn.cloud.customer.model.po.SupplierInfoQuery;
import com.xtechcn.cloud.customer.model.po.SupplierParam;
import com.xtechcn.cloud.customer.service.SupplierService;
import com.xtechcn.common.autoconfigure.supports.PublicParamResolver;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.utils.ICollUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
* 供应商管理
*
* @author hanjie
* @since 2025-09-16 09:36:46
*/
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    private final RemoteUserService remoteUserService;
    @Override
    public Supplier conventEntity(SupplierParam supplierParam) {
        return new Supplier()
                .name(supplierParam.getName()).usci(supplierParam.getUsci())
                .juridicalUser(supplierParam.getJuridicalUser()).address(supplierParam.getAddress())
                .areaId(supplierParam.getAreaId()).certStartTime(supplierParam.getCertStartTime())
                .certEndTime(supplierParam.getCertEndTime()).shopImage(supplierParam.getShopImage())
                .contactPhone(supplierParam.getContactPhone()).contactName(supplierParam.getContactName())
                .contactEmail(supplierParam.getContactEmail()).license(supplierParam.getLicense())
                .manageType(supplierParam.getManageType()).openId(supplierParam.getOpenId());
    }

    @Override
    public void uniqueCheck(Supplier supplier) {
        List<Supplier> suppliers = this.list(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getUsci, supplier.getUsci()));
        if (ICollUtil.isEmpty(suppliers)) return;
        for (Supplier source : suppliers) {
            if (source.equalsAndExcludeSelf(supplier)) throw new UniqueException();
        }
    }

    @Override
    public void upsertInit(Supplier supplier, String userId, Integer state) {
        supplier.userId(userId).state(state);
        if (ObjectUtils.isEmpty(supplier.getOpenId())){
            // 获取openId
            UserProfile profile = remoteUserService.profile(userId);
            supplier.openId(profile.getOpenid());
        }
    }

    @Override
    public Supplier infoQuery(SupplierInfoQuery supplierInfoQuery) {
        Supplier supplier = this.getOne(Wrappers.<Supplier>lambdaQuery()
                .eq(Supplier::getOpenId, supplierInfoQuery.getOpenId()));
        return supplier;
    }

    @Override
    public void check(SupplierParam supplierParam) {
        if (ObjectUtils.isEmpty(supplierParam.getOpenId())){
            throw new ParamException(CustomerResult.OPENID_NOT_NULL);
        }
    }

    @Override
    public void register(Supplier supplier) {
        // 密码前缀
        String pwdPrefix = PublicParamResolver.getValue(CustomerConstants.SUPPLIER_PASSWORD_KEY, String.class,
                CustomerConstants.DEFAULT_PASSWORD_PREFIX);
        // 创建用户账户
        AddAccountModel.MobileAccount defaultAccount = AddAccountModel.ofMobile()
                .nickname(supplier.getName())
                .mobile(supplier.getContactPhone())
                // 默认密码：Supp + 手机号码
                .password(pwdPrefix.concat(supplier.getContactPhone()))
                .avatar(supplier.getShopImage()).build();
        // 创建用户账户
        BaseUserEntity account = remoteUserService.createAccount(defaultAccount);
        // 绑定用户
        Supplier newSupplier = supplier.withId().userId(account.userId());
        this.updateById(newSupplier);
    }
}
