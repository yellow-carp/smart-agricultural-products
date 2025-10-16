package com.xtechcn.cloud.payment.core.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.ijpay.alipay.AliPayApi;
import com.xtechcn.cloud.payment.core.resolver.AlipayResultResolver;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.commom.payment.model.XPayNotify;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 支付宝 PC 支付
 *
 * @author Alay
 * @since 2023-06-11 02:14
 */
public class XPayAliPcPayService extends AbstractAliPayService {
    private static final String PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";

    public XPayAliPcPayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                               XPaySuccessHandler paySuccessHandler, AlipayResultResolver resultResolver) {
        super(channel, channelRepository, payFailureHandler, paySuccessHandler, resultResolver);
    }


    @Override
    public Object executePay(XPayTradeModel tradeModel, HttpServletRequest request, HttpServletResponse response) throws XPayException {
        AlipayTradePagePayModel pcPayModel = new AlipayTradePagePayModel();

        pcPayModel.setOutTradeNo(tradeModel.tradeNo());
        // 此参数固定值,不能修改，否则会提示订单无法识别
        pcPayModel.setProductCode(PRODUCT_CODE);
        // 元为单位
        pcPayModel.setTotalAmount(tradeModel.payAmount().toYuan().toString());
        pcPayModel.setSubject(tradeModel.subject());
        pcPayModel.setBody(tradeModel.body());
        // 支付宝原样返回,作为中转使用
        pcPayModel.setPassbackParams(tradeModel.channelKey());

        AlipayClient client = this.findClient(tradeModel.channelKey());

        XPayNotify xPayNotify = tradeModel.notifyUrl();
        try {
            AliPayApi.tradePage(client, response, pcPayModel, xPayNotify.callbackUrl(), xPayNotify.returnUrl());
            return true;
        } catch (AlipayApiException | IOException | RuntimeException ex) {
            logger.error("ali pay Failure, message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

}
