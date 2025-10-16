package com.xtechcn.cloud.payment.core.wx;

import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import com.xtechcn.cloud.payment.core.resolver.WxPayResultResolver;
import com.xtechcn.commom.payment.channel.XPayChannelNotify;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.constants.XPayConstants;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.commom.payment.model.conf.WxPayConfModel;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Optional;

/**
 * 微信扫码支付（NATIVE 支付）
 *
 * @author Alay
 * @since 2023-06-11 03:28
 */
public class XPayWxNativePayService extends AbstractWxPayService {

    public XPayWxNativePayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                                  XPaySuccessHandler paySuccessHandler, WxPayResultResolver resultResolver) {
        super(channel, channelRepository, payFailureHandler, paySuccessHandler, resultResolver);
    }

    @Override
    public Object executePay(XPayTradeModel tradeModel, HttpServletRequest request, HttpServletResponse response) throws XPayException {
        // 微信配置信息
        WxPayConfModel wxConfig = this.findConfig(tradeModel.channelKey());

        // 交易过期时间
        String expireTime = tradeModel.attribute(XPayConstants.ORDER_EXPIRE_TIME_KEY).toString();

        // 回调地址，优先获取配置中的，若配置中没有则获取全局的
        XPayChannelNotify notifyUrl = wxConfig.getNotifyUrl();
        String notify_url = Optional.ofNullable(notifyUrl.payNotify()).orElse((tradeModel.notifyUrl().callbackUrl()));

        // 构建微信的请求参数
        UnifiedOrderModel orderModel = UnifiedOrderModel.builder()
                .appid(wxConfig.getAppId())
                .mchid(wxConfig.getMchId())
                .description(tradeModel.body())
                .out_trade_no(tradeModel.tradeNo())
                .time_expire(expireTime)
                // 附加信息
                .attach(tradeModel.channelKey())
                // 对回调地址进行包装处理,支持多租户的情况
                .notify_url(this.notifyFormat(notify_url, tradeModel.channelKey()))
                // 付款金额 分
                .amount(Amount.builder().total(tradeModel.payAmount().intValue()).currency(tradeModel.currency()).build())
                .build();

        try {
            IJPayHttpResponse payResponse = WxPayApi.v3(RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.NATIVE_PAY.toString(),
                    wxConfig.getMchId(),
                    WxConfigTool.serialNo(wxConfig),
                    null,
                    privateKey(wxConfig),
                    IJsonUtil.toJson(orderModel));

            if (response.getStatus() != OK_STATUS) {
                logger.error("wx NATIVE pay err,response: [{}]", payResponse.getBody());
                // 支付下单失败
                throw new XPayException(payResponse.getBody());
            }
            // 成功：{"code_url":"weixin://wxpay/bizpayurl?pr=BaERZo0zz"}
            // 支付下单数据解析
            Map<String, String> nativeTradeMap = IJsonUtil.parseObject(payResponse.getBody(), Map.class);
            // 交易单号
            nativeTradeMap.put(XPayConstants.TRADE_NO_KEY, tradeModel.tradeNo());

            return nativeTradeMap;

        } catch (Exception ex) {
            logger.error("wx NATIVE pay err,message: [{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }
}
