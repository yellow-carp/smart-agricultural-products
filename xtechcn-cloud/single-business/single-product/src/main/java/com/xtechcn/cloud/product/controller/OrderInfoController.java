package com.xtechcn.cloud.product.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.product.model.po.PreOrderModelParam;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.lock.annotation.XLock4j;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.OrderInfo;
import com.xtechcn.cloud.product.service.OrderInfoService;
import com.xtechcn.common.redis.code.TicketValidate;
import com.xtechcn.common.security.constants.SecurityConst;
import com.xtechcn.common.security.utils.SecurityUtil;
import com.xtechcn.common.sequence.core.XSequenceUtil;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
* 订单表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/orderinfo")
@Tag(description = "orderinfo", name = "订单表")
public class OrderInfoController {

    private final  OrderInfoService orderInfoService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('pd:orderinfo:r')")
    public R pageQuery(Page page, OrderInfo orderInfo) {
        page = orderInfoService.page(page, Wrappers.lambdaQuery(orderInfo));
        return R.ok(page);
    }


    @GetMapping(value = "/details/{orderNo}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('pd:orderinfo:r')")
    public R getById(@PathVariable("orderNo") String orderNo) {
				// 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        OrderInfo orderInfo = orderInfoService.getById(orderNo);
        return R.ok(orderInfo);
    }

    @SysLog("生成预订单")
    @PostMapping(value = "/ordered/preview")
    @XLock4j(keys = {"#modelPo.getUserId()"})
    @Operation(description = "生成预订单", summary = "生成预订单")
    public R cartIndex(@RequestBody @Valid PreOrderModelParam modelParam) {
        /*
        // 验证用户是否使用手机号登录
        R<UserProfiles> profile = remoteAppService.profile(SecurityUtil.userId(), SecurityConst.INNER);
        UserProfiles userProfiles = ROptional.of(profile).ifPresent().orElseThrow(() -> new FeignCallException(profile.getMessage()));
        if (null == userProfiles.getPhone()) return R.failed(OrderResult.BIND_YOUR_MOBILE_PHONE_NUMBER_FIRST);
        // 取出二维数组集合，整合为一维数组
        OrderPo orderPo = orderPoHandle(modelParam);
        // 下单前价格展示
        TotalPriceVo totalPriceVo = orderInfoService.priceShow(orderPo);
        // 排序处理
        this.sortOrder(totalPriceVo, modelParam);
        //  生成Ticket
        String ticketId = XSequenceUtil.snowNo();
        String ticket = XSequenceUtil.snowNo();
        codeValidator.caching(TicketValidate.builder().ticket(ticket).identity(ticketId).build());
        totalPriceVo.setTicketId(ticketId);
        totalPriceVo.setTicket(ticket);
        */
        return R.ok();
    }


}