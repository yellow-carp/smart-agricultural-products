package com.xtechcn.cloud.payment.controller;

import cn.hutool.http.ContentType;
import com.xtechcn.cloud.payment.core.resolver.WxPayResultResolver;
import com.xtechcn.cloud.payment.core.resolver.WxRefundResultResolver;
import com.xtechcn.cloud.payment.model.dto.wx.WxPayResult;
import com.xtechcn.cloud.payment.model.dto.wx.WxRefundResult;
import com.xtechcn.cloud.payment.service.TradeRefundService;
import com.xtechcn.cloud.payment.service.TradeService;
import com.xtechcn.commom.payment.api.XPayIntegrationService;
import com.xtechcn.commom.payment.model.XPayRefundModel;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.common.security.annotation.OpenApi;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付
 *
 * @author Alay
 * @since 2025-08-19 10:22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/wxpay")
@Tag(description = "微信支付", name = "微信支付")
public class WxPayController {

    private final TradeService tradeService;
    private final TradeRefundService tradeRefundService;
    private final WxPayResultResolver payResultResolver;
    private final XPayIntegrationService xPayIntegrationService;
    private final WxRefundResultResolver refundResultResolver;

    @OpenApi
    @PostMapping(value = "/notify")
    @Operation(summary = "微信支付回调", description = "微信支付回调", hidden = true)
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 处理支付成功逻辑
        WxPayResult wxPayResult = payResultResolver.callbackResolve(request);
        if (wxPayResult.success()) {
            XPayTradeModel tradeModel = tradeService.findOne(wxPayResult.outTradeNo());
            Map<String, String> map = new HashMap<>(1 << 3);
            // 处理支付成功逻辑
            xPayIntegrationService.successful(tradeModel, wxPayResult);
            // 返回腾讯的
            response.setStatus(HttpStatus.OK.value());
            map.put("code", "SUCCESS");
            map.put("message", "SUCCESS");
            response.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.JSON.toString());
            response.getOutputStream().write(IJsonUtil.toJson(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        }
    }


    @OpenApi
    @PostMapping(value = "/refund/notify")
    @Operation(summary = "微信退款回调", description = "微信退款回调", hidden = true)
    public void wxNotifyRefund(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WxRefundResult xPayResult = refundResultResolver.callbackResolve(request);
        // 交易单数据
        XPayRefundModel refundModel = tradeRefundService.findOne(xPayResult.data().getOut_refund_no());
        // 退款成功回调
        tradeRefundService.successful(refundModel, xPayResult);
        if (xPayResult.success()) {
            Map<String, String> map = new HashMap<>(1 << 3);
            // 处理退款成功逻辑
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "SUCCESS");
            response.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.JSON.toString());
            response.getOutputStream().write(IJsonUtil.toJson(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        }
    }

}
