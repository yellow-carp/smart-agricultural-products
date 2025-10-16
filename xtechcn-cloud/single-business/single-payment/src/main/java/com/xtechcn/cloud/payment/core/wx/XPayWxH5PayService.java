package com.xtechcn.cloud.payment.core.wx;

import cn.hutool.core.io.IoUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.H5Info;
import com.ijpay.wxpay.model.v3.SceneInfo;
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
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 微信 H5 支付（MWEB 支付）
 *
 * @author Alay
 * @since 2023-06-11 03:28
 */
public class XPayWxH5PayService extends AbstractWxPayService {


    public XPayWxH5PayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                              XPaySuccessHandler paySuccessHandler, WxPayResultResolver resultResolver) {
        super(channel, channelRepository, payFailureHandler, paySuccessHandler, resultResolver);
    }

    @Override
    public Object executePay(XPayTradeModel tradeModel, HttpServletRequest request, HttpServletResponse response) throws XPayException {
        // 微信支付配置
        WxPayConfModel wxConfig = this.findConfig(tradeModel.channelKey());

        H5Info h5Info = H5Info.builder()
                // 场景类型
                .type("wap")
                // 支付回调域名
                .app_url(wxConfig.getNotifyUrl().payNotify())
                // 应用名称
                .app_name("h5支付").build();

        SceneInfo sceneInfo = SceneInfo.builder().h5_info(h5Info)
                .payer_client_ip(tradeModel.attribute(XPayConstants.CLIENT_IP_KEY).toString()).build();


        // 回调地址，优先获取配置中的，若配置中没有则获取全局的
        XPayChannelNotify notifyUrl = wxConfig.getNotifyUrl();
        String notify_url = Optional.ofNullable(notifyUrl.payNotify()).orElse((tradeModel.notifyUrl().callbackUrl()));

        // 构建微信的请求参数
        UnifiedOrderModel orderModel = UnifiedOrderModel.builder()
                .appid(wxConfig.getAppId())
                .mchid(wxConfig.getMchId())
                .description(tradeModel.body())
                .out_trade_no(tradeModel.tradeNo())
                // 附加信息
                .attach(tradeModel.channelKey())
                // 对回调地址进行包装处理,支持多租户的情况
                .notify_url(this.notifyFormat(notify_url, tradeModel.channelKey()))
                // 支付金额 分
                .amount(Amount.builder().total(tradeModel.payAmount().intValue()).currency(tradeModel.currency()).build())
                .scene_info(sceneInfo)
                .build();

        try {
            IJPayHttpResponse payResponse = WxPayApi.v3(RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.H5_PAY.toString(),
                    wxConfig.getMchId(),
                    WxConfigTool.serialNo(wxConfig),
                    null,
                    PayKit.getPrivateKeyByKeyContent(wxConfig.getKeyText(), AuthTypeEnum.RSA.getCode()),
                    IJsonUtil.toJson(orderModel));

            // 根据证书序列号查询对应证书来验证签名结果
            boolean isPass = WxPayKit.verifySignature(payResponse, IoUtil.toStream(wxConfig.getCertText().getBytes()));

            if (response.getStatus() != OK_STATUS || !isPass) {
                logger.error("wx h5 pay err,response: [{}]", payResponse.getBody());
                // 支付下单失败
                throw new XPayException(payResponse.getBody());
            }
            // 支付下单数据解析
            Map<String, Object> body = IJsonUtil.parseObject(payResponse.getBody(), HashMap.class);
            String h5Url = IMapUtil.getStr(body, "h5_url");

            return WxPayKit.jsApiCreateSign(wxConfig.getAppId(), h5Url, privateKey(wxConfig));
        } catch (Exception ex) {
            // 支付失败处理拦截器
            logger.error("wx h5 pay err,message: [{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

}
