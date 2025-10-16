package com.xtechcn.cloud.payment.core.converter;

import com.xtechcn.cloud.api.system.RemoteUserService;
import com.xtechcn.cloud.api.system.model.UserProfile;
import com.xtechcn.cloud.payment.constants.AttributeKeyNames;
import com.xtechcn.commom.payment.channel.XPayChannel;
import com.xtechcn.commom.payment.channel.XPayChannelNotify;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.config.properties.XPayConfigProperties;
import com.xtechcn.commom.payment.constants.XPayChannelEnum;
import com.xtechcn.commom.payment.constants.XPayConstants;
import com.xtechcn.commom.payment.converter.DefaultTradModelConverter;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.DefaultTradeModel;
import com.xtechcn.commom.payment.model.XPayNotify;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.commom.payment.model.conf.WxPayConfModel;
import com.xtechcn.commom.payment.model.parameter.ArousePayParameter;
import com.xtechcn.common.security.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 微信支付参数转换
 *
 * @author Alay
 * @since 2023-06-11 18:44
 */
@RequiredArgsConstructor
public class WxPayTradeModelConverter extends DefaultTradModelConverter {

    private final RemoteUserService remoteUserService;
    private final XPayConfigProperties xPayConfigProperties;
    private final XPayChannelRepository xPayChannelRepository;

    @Override
    public XPayTradeModel convert(HttpServletRequest request, Object parameters) {
        ArousePayParameter arousePayParameter = (ArousePayParameter) parameters;
        XPayChannelEnum payChannel = XPayChannelEnum.channelOf(arousePayParameter.getChannel());
        // 微信支付类型匹配
        if (payChannel != XPayChannelEnum.WX_JSAPI &&
                payChannel != XPayChannelEnum.WX_NATIVE &&
                payChannel != XPayChannelEnum.WX_MICROPAY &&
                payChannel != XPayChannelEnum.WX_MWEB) {
            return null;
        }

        DefaultTradeModel tradeModel = (DefaultTradeModel) super.convert(request, arousePayParameter);
        // 通知地址信息对象
        XPayNotify xPayNotify = tradeModel.notifyUrl();
        // 全局配置信息
        XPayConfigProperties.WxPay wxPayConf = xPayConfigProperties.getWxPay();
        // 支付成功回调地址
        xPayNotify.setCallbackUrl(wxPayConf.getNotifyUrl().payNotify());
        // 订单过期时间
        tradeModel.addAttribute(XPayConstants.ORDER_EXPIRE_TIME_KEY, xPayConfigProperties.getExpireTime());

        // 渠道配置信息
        XPayChannel config = xPayChannelRepository.findOne(tradeModel.channelKey());
        if (null == config) throw new XPayException("tenant un configuration payment channel");
        WxPayConfModel wxConfig = xPayChannelRepository.transform(config, WxPayConfModel.class);
        XPayChannelNotify notifyUrl = wxConfig.getNotifyUrl();
        String returnUrl = notifyUrl.attribute("returnUrl");
        if (null != returnUrl) {
            xPayNotify.setReturnUrl(returnUrl);
        }

        tradeModel.setNotifyUrl(xPayNotify);
        // mchId
        tradeModel.addAttribute(AttributeKeyNames.WX_MCHID, wxConfig.getMchId())
                // appId
                .addAttribute(AttributeKeyNames.APP_ID, wxConfig.getAppId());

        // 获取用户信息
        UserProfile userProfiles = remoteUserService.fatProfile(SecurityUtil.userId());
        if (null == userProfiles) throw new XPayException("user type not supported");
        // 微信 openid（可能会是 uniid）
        String openId = userProfiles.wxOpenid();
        if (null == openId || openId.isBlank()) throw new XPayException("wx openid not be blank");

        return tradeModel.addAttribute(AttributeKeyNames.WX_OPEN_ID, openId).<DefaultTradeModel>toThis();
    }

}
