package com.xtechcn.cloud.payment.core.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.xtechcn.cloud.payment.core.resolver.AlipayResultResolver;
import com.xtechcn.commom.payment.api.AbstractXPayService;
import com.xtechcn.commom.payment.channel.XPayChannel;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.commom.payment.model.DefaultXPayResult;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.serialization.tools.IJsonUtil;

import java.util.Map;

/**
 * 支付宝支付
 *
 * @author Alay
 * @since 2023-06-11 01:34
 */
public abstract class AbstractAliPayService extends AbstractXPayService {
    /**
     * 支付成功查询响应码
     */

    private final AlipayResultResolver resultResolver;


    public AbstractAliPayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                                 XPaySuccessHandler paySuccessHandler, AlipayResultResolver resultResolver) {
        super(channel, paySuccessHandler, payFailureHandler, channelRepository);
        this.resultResolver = resultResolver;
    }

    @Override
    public XPayResult<?> queryPayResult(XPayTradeModel tradeModel) throws XPayException {
        // 查询支付宝支付的请求体
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        // 请求参数数据体
        Map<String, String> bizContent = IMapUtil.hashMap(1);
        bizContent.put("out_trade_no", tradeModel.tradeNo());
        request.setBizContent(IJsonUtil.toJson(bizContent));

        // 支付宝客户端
        AlipayClient client = this.findClient(tradeModel.channelKey());
        // 执行查询支付结果
        try {
            AlipayTradeQueryResponse response = client.execute(request);
            if (response.isSuccess()) {
                // 响应数据转换
                return resultResolver.queryResolve(response);
            }
            // 支付查询失败
            return DefaultXPayResult.Pay.builder().success(false).build();
        } catch (AlipayApiException ex) {
            logger.error("alipay query pay_result err, message:[{}]", ex.getMessage());
            // 支付查询失败
            return DefaultXPayResult.Pay.builder().success(false).error(ex).build();
        }
    }


    protected AlipayClient findClient(String channelKey) {
        // 支付宝配置信息
        XPayChannel xPayChannel = channelRepository.findOne(channelKey);
        // 支付配置信息转换
        AliPayApiConfig config = channelRepository.transform(xPayChannel, AliPayApiConfig.class);

        return new DefaultAlipayClient(config.getServiceUrl(),
                config.getAppId(),
                config.getPrivateKey(),
                "json",
                config.getCharset(),
                config.getAliPayPublicKey(),
                config.getSignType());
    }


    @Override
    public void closeTrade(XPayTradeModel tradeModel) throws XPayException {
        // 调用支付宝接口关闭支付宝中的交易
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(tradeModel.tradeNo());
        // 支付宝客户端
        AlipayClient client = this.findClient(tradeModel.channelKey());
        try {
            // 调用支付宝关闭接口关闭交易单
            String body = AliPayApi.tradeCloseToResponse(client, true, model).getBody();
            logger.trace("alipay close trade success,response:[{}]", body);
            // 执行未支付的逻辑
            payFailureHandler.onFailure(tradeModel, new XPayException("user not paid"));
        } catch (Exception ex) {
            // 执行未支付的逻辑
            payFailureHandler.onFailure(tradeModel, new XPayException("user not paid", ex));
            logger.error("alipay close trade error,message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }


}
