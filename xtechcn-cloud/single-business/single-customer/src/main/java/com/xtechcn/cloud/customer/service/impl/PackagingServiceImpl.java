package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.customer.constants.CacheConstants;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.entity.Packaging;
import com.xtechcn.cloud.customer.entity.Unit;
import com.xtechcn.cloud.customer.mapper.PackagingMapper;
import com.xtechcn.cloud.customer.model.po.PackagingParam;
import com.xtechcn.cloud.customer.model.vo.PackagingView;
import com.xtechcn.cloud.customer.service.PackagingService;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.result.FailedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 包装形式
 *
 * @author hanjie
 * @since 2025-09-11 17:59:28
 */
@Service
@RequiredArgsConstructor
public class PackagingServiceImpl extends ServiceImpl<PackagingMapper, Packaging> implements PackagingService {
    private final RedisTemplate redisTemplate;
    @Override
    public Packaging conventEntity(PackagingParam packagingParam) {
        Packaging packaging = new Packaging();
        packaging.setId(packagingParam.getId());
        packaging.setName(packagingParam.getName());
        packaging.setLength(packagingParam.getLength());
        packaging.setWidth(packagingParam.getWidth());
        packaging.setHeight(packagingParam.getHeight());
        return packaging;
    }

    @Override
    public void uniqueCheck(Packaging packaging) {
        List<Packaging> packagings = this.list();
        if (packagings.isEmpty()) return;
        for (Packaging pk : packagings) {
            if (pk.equalsAndExcludeSelf(packaging)) {
                throw new UniqueException("已存在相同包装信息");
            }
        }
    }

    @Override
    public List<PackagingView> returnViewHandler(List<Packaging> packagingList) {
        if (packagingList.isEmpty() || packagingList == null) return new ArrayList<>();
        List<PackagingView> packagingViewList = new ArrayList<>();
        for (Packaging packaging : packagingList) {
            PackagingView packagingView = new PackagingView();
            packagingView.setId(packaging.getId());
            packagingView.setName(packaging.getName());
            packagingView.setLength(packaging.getLength());
            packagingView.setWidth(packaging.getWidth());
            packagingView.setHeight(packaging.getHeight());
            packagingView.setEnabled(packaging.getEnabled());
            packagingViewList.add(packagingView);
        }
        return packagingViewList;
    }

    @Override
    public void exist(Packaging packaging) {
        Packaging pack = this.getById(packaging);
        if (pack == null) throw new UniqueException(FailedResult.NOT_FOUNT);
    }

    @Override
    @Cacheable(value = CacheConstants.PACKAGING_CACHE_KEY, key = "#id", unless = "#result == null")
    public Packaging findAndCache(String id) {
        return this.getById(id);
    }

    @Override
    @CacheEvict(value = CacheConstants.PACKAGING_CACHE_KEY, key = "#entity.getId()")
    public boolean updateById(Packaging entity) {
        return 1 == this.baseMapper.updateById(entity);
    }

    @Override
    public void removeCache(String id) {
        redisTemplate.delete(CacheConstants.PACKAGING_CACHE_KEY + "::" + id);
    }
}
