package com.xtechcn.cloud.payment.controller;

import com.xtechcn.cloud.payment.service.TradeService;
import com.xtechcn.commom.payment.api.XPayIntegrationService;
import com.xtechcn.commom.payment.checker.XPayTradeModelChecker;
import com.xtechcn.commom.payment.converter.XPayTradModelConverter;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.commom.payment.model.parameter.ArousePayParameter;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.lock.annotation.XLock4j;
import com.xtechcn.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聚合支付
 *
 * @author Alay
 * @since 2025-08-01 16:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/aggregate")
@Tag(description = "聚合支付", name = "聚合支付")
public class AggregateController {

    private final TradeService tradeService;
    private final XPayTradeModelChecker tradeDataChecker;
    private final XPayTradModelConverter tradModelConverter;
    private final XPayIntegrationService xPayIntegrationService;

    @Transactional
    @SysLog(value = "移动端支付")
    @PostMapping(value = "/mobile/pay")
    @XLock4j(keys = {"#request.getHeader('Authentication')"})
    @Operation(summary = "移动端支付", description = "移动端支付")
    public R<?> mobilePay(@RequestBody ArousePayParameter payParam, HttpServletRequest request, HttpServletResponse response) {
        // 支付参数转换
        XPayTradeModel tradeModel = tradModelConverter.convert(request, payParam);
        // 参数验证,后得到新的请求参数
        tradeModel = tradeDataChecker.check(tradeModel);
        // 交易单数据处理
        tradeModel = tradeService.generate(tradeModel);
        // 支付调起
        Object result = xPayIntegrationService.executePay(tradeModel, request, response);
        return R.ok(result);
    }

}
