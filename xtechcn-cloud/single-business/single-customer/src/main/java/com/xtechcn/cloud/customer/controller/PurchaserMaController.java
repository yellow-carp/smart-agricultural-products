package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.api.system.RemoteUserService;
import com.xtechcn.cloud.api.system.model.UserProfile;
import com.xtechcn.cloud.customer.constants.CustomerConstants;
import com.xtechcn.cloud.customer.constants.PrOrderStateEnum;
import com.xtechcn.cloud.customer.constants.PurchaserStateEnum;
import com.xtechcn.cloud.customer.entity.Purchaser;
import com.xtechcn.cloud.customer.entity.PurchaserOrder;
import com.xtechcn.cloud.customer.model.TradeCheckModel;
import com.xtechcn.cloud.customer.model.TradeNotifyModel;
import com.xtechcn.cloud.customer.model.po.PurchaserMaParam;
import com.xtechcn.cloud.customer.model.vo.PurchaserView;
import com.xtechcn.cloud.customer.service.PurchaserOrderService;
import com.xtechcn.cloud.customer.service.PurchaserService;
import com.xtechcn.common.core.lang.Disenable;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 采购商管理
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/purchaser/ma")
@Tag(description = "purchaser-ma", name = "采购商管理")
public class PurchaserMaController {

    private final PurchaserService purchaserService;
    private final RemoteUserService remoteUserService;
    private final PurchaserOrderService purchaserOrderService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:purchaser:r')")
    public R<Page<PurchaserView>> pageQuery(Page page, Purchaser purchaser) {
        page = purchaserService.page(page, Wrappers.lambdaQuery(purchaser).orderByDesc(Purchaser::getCreateTime));
        List<Purchaser> records = page.getRecords();
        if (ICollUtil.isNotEmpty(records)) {
            List<String> userIds = ICollUtil.process2List(records, Purchaser::getUserId);
            List<UserProfile> profiles = remoteUserService.profiles(userIds);
            Map<String, UserProfile> userProfileMap = IMapUtil.coll2Map(profiles, UserProfile::getUserId);
            List<PurchaserView> purchaserViewList = purchaserService.dataHandle(records, userProfileMap);
            page.setRecords(purchaserViewList);
        }
        return R.ok(page);
    }

    @SysLog("新增修改采购商管理")
    @PostMapping(value = "/upsert")
    @Operation(description = "新增修改采购商管理", summary = "新增修改采购商管理")
    @PreAuthorize("@pmh.hasPermission('ct:purchaser:w')")
    public R updateById(@RequestBody @Valid PurchaserMaParam purchaserMaParam) {
        // 参数转实体
        Purchaser purchaser = purchaserService.conventMaEntity(purchaserMaParam);
        // 唯一性校验
        purchaserService.uniqueCheck(purchaser);
        // 采购商注册方式
        if (purchaserMaParam.isAdd()) purchaser.registerModel(CustomerConstants.PURCHASER_MODEL_ADMIN);
        boolean isOk = purchaserMaParam.isAdd() ? purchaserService.save(purchaser) : purchaserService.updateById(purchaser);
        return R.ok(isOk);
    }

    @PostMapping(value = "/disenable")
    @Operation(description = "启停采购商", summary = "启停采购商")
    @PreAuthorize("@pmh.hasPermission('ct:purchaser:w')")
    public R<Boolean> disenable(@RequestBody Disenable disenable) {
        Purchaser purchaser = purchaserService.getById(disenable.ofId());
        if (null == purchaser) return R.failed(FailedResult.NOT_FOUNT);
        if (Objects.equals(purchaser.getEnabled(), disenable.enabled())) {
            return R.ok(true);
        }
        Purchaser newPurchaser = purchaser.withId()
                .userId(purchaser.getUserId())
                .enabled(disenable.enabled());
        boolean isOk = purchaserService.updateById(newPurchaser);
        return R.ok(isOk);
    }

    @PostMapping(value = "/trade/verify")
    @Operation(description = "采购商入驻交易单验证", summary = "采购商入驻交易单验证", hidden = true)
    public R verifyTrade(@RequestBody TradeCheckModel tradeCheck) {
        tradeCheck = purchaserOrderService.checkDataMsg(tradeCheck);
        return R.ok(tradeCheck);
    }

    @PostMapping(value = "/trade/success")
    @Operation(description = "支付成功回调通知", summary = "支付成功回调通知", hidden = true)
    public R tradeSuccess(@RequestBody TradeNotifyModel successModel) {
        // 采购商入驻交易单只有一个订单
        PurchaserOrder purchaserOrder = purchaserOrderService.listByTradeNo(successModel.getTradeNo()).getFirst();
        boolean isOk = purchaserOrderService.updateById(purchaserOrder.newInstance().state(PrOrderStateEnum.PAYED.getCode()));
        // 更新采购商状态
        purchaserService.updateState(purchaserOrder.getUserId(), PurchaserStateEnum.AUDIT.getCode(), CustomerConstants.PURCHASER_MODEL_PAY);
        return R.ok(isOk);
    }

    @PostMapping(value = "/trade/failure")
    @Operation(description = "支付失败回调通知", summary = "支付失败回调通知", hidden = true)
    public R tradeFailure(@RequestBody TradeNotifyModel failureModel) {
        // 采购商入驻交易单只有一个订单
        PurchaserOrder purchaserOrder = purchaserOrderService.listByTradeNo(failureModel.getTradeNo()).getFirst();
        boolean isOk = purchaserOrderService.updateById(purchaserOrder.newInstance().state(PrOrderStateEnum.TO_BE_PAID.getCode()));
        return R.ok(isOk);
    }
}