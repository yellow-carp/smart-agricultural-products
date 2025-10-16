package com.xtechcn.cloud.payment.core.wx;

import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.Payer;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import com.xtechcn.cloud.payment.constants.AttributeKeyNames;
import com.xtechcn.cloud.payment.core.resolver.WxPayResultResolver;
import com.xtechcn.commom.payment.channel.XPayChannelNotify;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.constants.XPayConstants;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.commom.payment.model.conf.WxPayConfModel;
import com.xtechcn.common.core.utils.IDateUtil;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 微信公众号支付或者小程序支付 ( JSAPI 支付)
 * <a href="https://gitee.com/javen205/IJPay">IJPay文档</a>
 * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3_partner/pages/index.shtml">微信官方文档</a>
 *
 * @author Alay
 * @since 2023-06-11 03:28
 */
public class XPayWxJsApiPayService extends AbstractWxPayService {


    public XPayWxJsApiPayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                                 XPaySuccessHandler paySuccessHandler, WxPayResultResolver resultResolver) {
        super(channel, channelRepository, payFailureHandler, paySuccessHandler, resultResolver);
    }

    /**
     * @param tradeModel 交易单数据
     * @param request    支付请求
     * @param response   响应
     * @return
     * @throws XPayException
     * @see <a href="https://pay.weixin.qq.com/docs/partner/apis/partner-jsapi-payment/partner-jsons/partner-jsapi-prepay.html">微信文档</a>
     */
    @Override
    public Object executePay(XPayTradeModel tradeModel, HttpServletRequest request, HttpServletResponse response) throws XPayException {
        // 微信支付配置
        WxPayConfModel wxConfig = this.findConfig(tradeModel.channelKey());
        // 交易过期时间
        long expireTime = tradeModel.attribute(XPayConstants.ORDER_EXPIRE_TIME_KEY);
        LocalDateTime timeExpire = LocalDateTime.now().plusSeconds(expireTime);

        // 回调地址，优先获取配置中的，若配置中没有则获取全局的
        XPayChannelNotify notifyUrl = wxConfig.getNotifyUrl();
        String notify_url = Optional.ofNullable(notifyUrl.payNotify()).orElse((tradeModel.notifyUrl().callbackUrl()));

        // 构建微信的请求参数
        UnifiedOrderModel orderModel = UnifiedOrderModel.builder()
                .appid(wxConfig.getAppId())
                .mchid(wxConfig.getMchId())
                .description(tradeModel.body())
                .out_trade_no(tradeModel.tradeNo())
                .time_expire(IDateUtil.time2Rfc3339(timeExpire))
                // 附加信息(回调时原样返回)
                .attach(tradeModel.channelKey())
                // 对回调地址进行包装处理,支持多租户的情况
                .notify_url(this.notifyFormat(notify_url, tradeModel.channelKey()))
                // 付款金额 分
                .amount(Amount.builder().total(tradeModel.payAmount().intValue()).currency(tradeModel.currency()).build())
                // .amount(Amount.builder().total(1).currency(tradeModel.currency()).build())
                // 付款者微信个人Id
                .payer(Payer.builder().openid(tradeModel.attribute(AttributeKeyNames.WX_OPEN_ID).toString()).build())
                .build();

        try {
            IJPayHttpResponse payResponse = WxPayApi.v3(RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    // 正式路径
                    BasePayApiEnum.JS_API_PAY.toString(),
                    wxConfig.getMchId(),
                    WxConfigTool.serialNo(wxConfig),
                    null,
                    privateKey(wxConfig),
                    IJsonUtil.toJson(orderModel));

            // 根据证书序列号查询对应的证书来验证签名结果
            if (response.getStatus() != OK_STATUS) {
                logger.error("wx JSAPI Pay  Failed response: {}", payResponse.getBody());
                // 支付下单失败
                throw new XPayException(payResponse.getBody());
            }
            // 支付下单数据解析
            Map<String, Object> body = IJsonUtil.parseObject(payResponse.getBody(), HashMap.class);
            String prepayId = IMapUtil.getStr(body, AttributeKeyNames.WX_PREPAY_ID);

            return WxPayKit.jsApiCreateSign(wxConfig.getAppId(), prepayId, privateKey(wxConfig));
        } catch (Exception ex) {
            logger.error("wx JSAPI pay err,message: [{}]]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

}
