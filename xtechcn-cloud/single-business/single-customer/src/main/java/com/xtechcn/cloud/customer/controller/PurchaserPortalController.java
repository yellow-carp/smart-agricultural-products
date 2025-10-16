package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.api.system.RemoteUserService;
import com.xtechcn.cloud.api.system.model.UserProfile;
import com.xtechcn.cloud.customer.constants.CustomerResult;
import com.xtechcn.cloud.customer.entity.PurchaserConfig;
import com.xtechcn.cloud.customer.entity.PurchaserOrder;
import com.xtechcn.cloud.customer.model.po.PrCheckCodeParam;
import com.xtechcn.cloud.customer.model.po.PrOrderParam;
import com.xtechcn.cloud.customer.model.po.PurchaserParam;
import com.xtechcn.cloud.customer.model.vo.OrderVerifyView;
import com.xtechcn.cloud.customer.service.PurchaserConfigService;
import com.xtechcn.cloud.customer.service.PurchaserOrderService;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.lock.annotation.XLock4j;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.Purchaser;
import com.xtechcn.cloud.customer.service.PurchaserService;
import com.xtechcn.common.security.utils.SecurityUtil;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 采购商管理
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/purchaser/portal")
@Tag(description = "purchaser-portal", name = "小程序-采购商管理")
public class PurchaserPortalController {

    private final PurchaserService purchaserService;
    private final RemoteUserService remoteUserService;
    private final PurchaserOrderService purchaserOrderService;
    private final PurchaserConfigService purchaserConfigService;

    @GetMapping(value = "/order/page")
    @Operation(description = "分页查询采购商入驻订单", summary = "分页查询采购商入驻订单")
    public R<Page<PurchaserOrder>> pageQuery(Page page, PurchaserOrder purchaserOrder) {
        page = purchaserOrderService.page(page, Wrappers.lambdaQuery(purchaserOrder).orderByDesc(PurchaserOrder::getCreateTime));
        return R.ok(page);
    }

    @Transactional
    @SysLog("生成采购商入驻订单")
    @PostMapping(value = "/ordered")
    @XLock4j(keys = {"#prOrderParam.getUserId()"})
    @Operation(description = "生成采购商入驻订单", summary = "生成采购商入驻订单")
    public R ordered(@RequestBody @Valid PrOrderParam prOrderParam) {
        // 验证用户是否使用手机号登录
        UserProfile profile = remoteUserService.profile(prOrderParam.getUserId());
        if (null == profile.getPhone()) return R.failed(CustomerResult.BIND_YOUR_MOBILE_PHONE_NUMBER_FIRST);
        // 查询采购商入驻配置
        PurchaserConfig config = purchaserConfigService.getConfig();
        if (null == config || null == config.getRegisterAmount()) return R.failed(CustomerResult.CONFIG_NOT_FOUND);
        // 生成供应商入驻订单
        OrderVerifyView orderVerifyView = purchaserOrderService.applyPurchaserOrder(prOrderParam, profile, config);
        // 添加支付交易单验证链接
        return R.ok(orderVerifyView);
    }
    @Transactional
    @SysLog("采购商申请")
    @PostMapping(value = "/apply")
    @Operation(description = "采购商申请", summary = "采购商申请")
    public R<Boolean> save(@RequestBody @Valid PurchaserParam purchaserParam) {
        // 参数转换
        Purchaser purchaser = purchaserService.conventEntity(purchaserParam);
        // 数据唯一性验证
        purchaserService.uniqueCheck(purchaser);
        // 数据初始化
        purchaserService.upsertInit(purchaser);
        // 数据入库
        boolean isOk = purchaserParam.isAdd() ? purchaserService.save(purchaser) : purchaserService.updateById(purchaser);
        return R.ok(isOk);
    }

    @Transactional
    @PostMapping(value = "/check/code")
    @Operation(description = "采购商校验邀请码", summary = "采购商校验邀请码")
    public R<Boolean> checkCode(@RequestBody @Valid PrCheckCodeParam prCheckCodeParam) {
        // 查询登录的采购商
        String userId = SecurityUtil.userId();
        Purchaser purchaser = purchaserService.findUserId(userId);
        if (null == purchaser) return R.failed(FailedResult.NOT_FOUNT);
        // 查询系统配置的邀请码
        PurchaserConfig config = purchaserConfigService.getConfig();
        if (null == config) return R.failed(CustomerResult.CONFIG_NOT_FOUND);
        boolean isOk = purchaserService.checkCode(purchaser, prCheckCodeParam, config);
        return R.ok(isOk);
    }

    @GetMapping(value = "/state")
    @Operation(description = "查询登录的采购商状态", summary = "查询登录的采购商状态")
    public R<Purchaser> getSupplier() {
        Purchaser purchaser = purchaserService.findUserId(SecurityUtil.userId());
        return R.ok(purchaser);
    }
}