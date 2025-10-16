package com.xtechcn.cloud.payment.core.converter;

import com.xtechcn.cloud.payment.constants.AttributeKeyNames;
import com.xtechcn.commom.payment.channel.XPayChannel;
import com.xtechcn.commom.payment.channel.XPayChannelNotify;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.config.properties.XPayConfigProperties;
import com.xtechcn.commom.payment.constants.XPayChannelEnum;
import com.xtechcn.commom.payment.converter.DefaultTradModelConverter;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.DefaultTradeModel;
import com.xtechcn.commom.payment.model.XPayNotify;
import com.xtechcn.commom.payment.model.XPayTradeModel;
import com.xtechcn.commom.payment.model.conf.AliPayConfModel;
import com.xtechcn.commom.payment.model.parameter.ArousePayParameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 支付宝支付参数转换
 *
 * @author Alay
 * @since 2023-06-11 18:44
 */
@RequiredArgsConstructor
public class AlipayTradeModelConverter extends DefaultTradModelConverter {

    private final XPayConfigProperties xPayConfigProperties;
    private final XPayChannelRepository xPayChannelRepository;

    @Override
    public XPayTradeModel convert(HttpServletRequest request, Object parameters) {
        ArousePayParameter arousePayParameter = (ArousePayParameter) parameters;
        XPayChannelEnum payChannel = XPayChannelEnum.channelOf(arousePayParameter.getChannel());
        // 支付宝支付类型匹配
        if (payChannel != XPayChannelEnum.ALI_WEB &&
                payChannel != XPayChannelEnum.ALI_WAP) {
            return null;
        }

        DefaultTradeModel tradeModel = (DefaultTradeModel) super.convert(request, arousePayParameter);

        // 回调等地址信心
        XPayNotify xPayNotify = tradeModel.notifyUrl();
        // 阿里支付配置
        XPayConfigProperties.AliPay aliPayConf = xPayConfigProperties.getAliPay();
        // 支付成功回调地址
        xPayNotify.setCallbackUrl(aliPayConf.getNotifyUrl().payNotify());
        // 前端页面跳转地址
        xPayNotify.setReturnUrl(aliPayConf.getNotifyUrl().returnUrl());

        // 阿里支付渠道配置
        XPayChannel config = xPayChannelRepository.findOne(tradeModel.channelKey());
        if (null == config) throw new XPayException("payment channel not supported");
        AliPayConfModel aliConfig = xPayChannelRepository.transform(config, AliPayConfModel.class);
        // appId
        tradeModel.addAttribute(AttributeKeyNames.APP_ID, aliConfig.getAppId());

        XPayChannelNotify notifyUrl = aliConfig.getNotifyUrl();
        if (null != notifyUrl.returnUrl() && !notifyUrl.returnUrl().isEmpty()) {
            // 前端页面跳转地址
            xPayNotify.setReturnUrl(notifyUrl.returnUrl());
        }

        tradeModel.setNotifyUrl(xPayNotify);
        return tradeModel;
    }
}
