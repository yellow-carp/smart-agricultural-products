package com.xtechcn.cloud.payment.core.ali;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
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

/**
 * 支付宝 WAP 支付
 *
 * @author Alay
 * @since 2023-06-11 02:14
 */
public class XPayAliWapPayService extends AbstractAliPayService {
    private static final String PRODUCT_CODE = "QUICK_WAP_PAY";

    public XPayAliWapPayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                                XPaySuccessHandler paySuccessHandler, AlipayResultResolver resultResolver) {
        super(channel, channelRepository, payFailureHandler, paySuccessHandler, resultResolver);
    }


    @Override
    public Object executePay(XPayTradeModel tradeModel, HttpServletRequest request, HttpServletResponse response) throws XPayException {
        AlipayTradeWapPayModel wapPayModel = new AlipayTradeWapPayModel();
        wapPayModel.setOutTradeNo(tradeModel.tradeNo());
        // 此参数固定值,不能修改，否则会提示订单无法识别
        wapPayModel.setProductCode(PRODUCT_CODE);
        wapPayModel.setTotalAmount(tradeModel.payAmount().toString());
        wapPayModel.setSubject(tradeModel.subject());
        wapPayModel.setBody(tradeModel.body());
        // 支付宝原样返回,作为中转使用
        wapPayModel.setPassbackParams(tradeModel.channelKey());
        try {
            // 支付宝客户端
            AlipayClient client = this.findClient(tradeModel.channelKey());

            XPayNotify xPayNotify = tradeModel.notifyUrl();

            AliPayApi.wapPay(client, response, wapPayModel, xPayNotify.returnUrl(), xPayNotify.callbackUrl());
            return true;
        } catch (Exception ex) {
            // 支付失败触发事件
            logger.error("ali pay Failure, message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }
}
