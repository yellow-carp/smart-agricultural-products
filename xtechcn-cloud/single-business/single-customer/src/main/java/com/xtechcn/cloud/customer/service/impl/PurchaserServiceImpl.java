package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.api.system.model.UserProfile;
import com.xtechcn.cloud.customer.constants.CacheConstants;
import com.xtechcn.cloud.customer.constants.CustomerResult;
import com.xtechcn.cloud.customer.constants.PurchaserStateEnum;
import com.xtechcn.cloud.customer.entity.Purchaser;
import com.xtechcn.cloud.customer.entity.PurchaserConfig;
import com.xtechcn.cloud.customer.mapper.PurchaserMapper;
import com.xtechcn.cloud.customer.model.po.PrCheckCodeParam;
import com.xtechcn.cloud.customer.model.po.PurchaserMaParam;
import com.xtechcn.cloud.customer.model.po.PurchaserParam;
import com.xtechcn.cloud.customer.model.vo.PurchaserView;
import com.xtechcn.cloud.customer.service.PurchaserService;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.security.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 采购商管理
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@Service
@RequiredArgsConstructor
public class PurchaserServiceImpl extends ServiceImpl<PurchaserMapper, Purchaser> implements PurchaserService {

    private final RedisTemplate<String, ?> redisTemplate;

    @Override
    public Purchaser conventEntity(PurchaserParam purchaserParam) {
        Purchaser purchaser = new Purchaser()
                .name(purchaserParam.getName()).usci(purchaserParam.getUsci())
                .juridicalUser(purchaserParam.getJuridicalUser()).address(purchaserParam.getAddress())
                .areaId(purchaserParam.getAreaId()).certStartTime(purchaserParam.getCertStartTime())
                .certEndTime(purchaserParam.getCertEndTime()).storefrontPhoto(purchaserParam.getStorefrontPhoto())
                .contactPhone(purchaserParam.getContactPhone()).contactName(purchaserParam.getContactName())
                .contactEmail(purchaserParam.getContactEmail()).license(purchaserParam.getLicense());
        // 填充用户id
        if (purchaserParam.isAdd()) {
            String userId = SecurityUtil.userId();
            purchaser.userId(userId);
        }
        return purchaser;
    }

    @Override
    public void uniqueCheck(Purchaser purchaser) {
        List<Purchaser> list = this.list(Wrappers.<Purchaser>lambdaQuery().eq(Purchaser::getUserId, purchaser.getUserId()));
        if (ICollUtil.isEmpty(list)) return;
        for (Purchaser source : list) {
            if (source.equalsAndExcludeSelf(purchaser)) throw new UniqueException();
        }
    }

    @Override
    public void upsertInit(Purchaser purchaser) {

    }

    @Override
    @Cacheable(value = CacheConstants.PURCHASER_CACHE_KEY, key = "#userId", unless = "#result == null")
    public Purchaser findUserId(String userId) {
        return this.getOne(Wrappers.<Purchaser>lambdaQuery().eq(Purchaser::getUserId, userId));
    }

    @Override
    @CacheEvict(value = CacheConstants.PURCHASER_CACHE_KEY, key = "#entity.userId")
    public boolean updateById(Purchaser entity) {
        return 1 == this.baseMapper.updateById(entity);
    }

    @Override
    public void removeCache(String userId) {
        redisTemplate.delete(CacheConstants.PURCHASER_CACHE_KEY.concat("::").concat(userId));
    }

    @Override
    public boolean checkCode(Purchaser purchaser, PrCheckCodeParam prCheckCodeParam, PurchaserConfig config) {
        // 校验邀请码
        if (!config.getInviteCode().equals(prCheckCodeParam.getCode())) {
            throw new ParamException(CustomerResult.INVALID_INVITATION_CODE.getMessage());
        }
        // 更新采购商状态为:已入驻
        purchaser.withId().state(PurchaserStateEnum.AUDIT.getCode());
        return this.updateById(purchaser);
    }

    @Override
    public List<PurchaserView> dataHandle(List<Purchaser> records, Map<String, UserProfile> userProfileMap) {

        return records.stream().map(record -> {
            PurchaserView purchaserView = new PurchaserView();
            purchaserView.setId(record.getId());
            purchaserView.setUserId(record.getUserId());
            purchaserView.setAvatar(userProfileMap.get(record.getUserId()).getAvatar());
            purchaserView.setUsci(record.getUsci());
            purchaserView.setName(record.getName());
            purchaserView.setContactName(record.getContactName());
            purchaserView.setContactPhone(record.getContactPhone());
            purchaserView.setEnabled(record.getEnabled());
            purchaserView.setRegisterAmount(new MoneyPenny(30000));
            return purchaserView;
        }).toList();
    }

    @Override
    public Purchaser conventMaEntity(PurchaserMaParam purchaserMaParam) {
        Purchaser purchaser = new Purchaser();
        purchaser.setId(purchaserMaParam.getId());
        purchaser.setUserId(purchaserMaParam.getUserId());
        purchaser.setUsci(purchaserMaParam.getUsci());
        purchaser.setName(purchaserMaParam.getName());
        purchaser.setJuridicalUser(purchaserMaParam.getJuridicalUser());
        purchaser.setAreaId(purchaserMaParam.getAreaId());
        purchaser.setContactName(purchaserMaParam.getContactName());
        purchaser.setContactPhone(purchaserMaParam.getContactPhone());
        purchaser.setLicense(purchaserMaParam.getLicense());
        return purchaser;

    }

    @Override
    public boolean updateState(String userId, Integer state, Integer registerModel) {
        Purchaser purchaser = this.findUserId(userId);
        if (null == purchaser) return true;
        Purchaser newPurchaser = purchaser.withId()
                .state(state)
                .userId(userId)
                .registerModel(registerModel);
        return this.updateById(newPurchaser);
    }
}
