package com.xtechcn.cloud.payment.config;

import com.xtechcn.cloud.api.system.RemoteUserService;
import com.xtechcn.cloud.payment.core.checker.DefaultTradeModelChecker;
import com.xtechcn.cloud.payment.core.checker.LimitAmountTradeChecker;
import com.xtechcn.cloud.payment.core.converter.AlipayTradeModelConverter;
import com.xtechcn.cloud.payment.core.converter.WxPayTradeModelConverter;
import com.xtechcn.cloud.payment.core.cpcn.XPayCpcnPayService;
import com.xtechcn.cloud.payment.core.handler.GoodsTradeTypeHandler;
import com.xtechcn.cloud.payment.core.resolver.CpcnPayResultResolver;
import com.xtechcn.cloud.payment.core.resolver.WxPayResultResolver;
import com.xtechcn.cloud.payment.core.wx.XPayWxJsApiPayService;
import com.xtechcn.commom.payment.api.DelegatingXPayService;
import com.xtechcn.commom.payment.api.XPayIntegrationService;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.checker.XPayTradeModelChecker;
import com.xtechcn.commom.payment.config.properties.XPayConfigProperties;
import com.xtechcn.commom.payment.constants.XPayChannelEnum;
import com.xtechcn.commom.payment.converter.XPayTradModelConverter;
import com.xtechcn.commom.payment.event.XPayEventPublisher;
import com.xtechcn.commom.payment.handler.TradeTypeHandler;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.common.cpcn.api.CpcnTxRequestExecutor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 支付模块配置
 *
 * @author Alay
 * @since 2025-08-19 10:28
 */
@Configuration(proxyBeanMethods = false)
public class PaymentConfiguration {

    /**
     * 交易单数据转换
     */
    @Bean
    public XPayTradModelConverter tradModelConverter(XPayConfigProperties xPayConfigProperties, XPayChannelRepository xPayChannelRepository,
                                                     RemoteUserService remoteUserService) {
        return XPayTradModelConverter.Delegating.of(
                // 支付宝交易参数转换
                new AlipayTradeModelConverter(xPayConfigProperties, xPayChannelRepository),
                // 微信支付
                new WxPayTradeModelConverter(remoteUserService, xPayConfigProperties, xPayChannelRepository));
    }

    /**
     * 交易单数据验证
     */
    @Bean
    public XPayTradeModelChecker tradeModelChecker() {
        // 金额限制交易单数据验证
        LimitAmountTradeChecker limitAmountTradeChecker = new LimitAmountTradeChecker();
        // 默认交易单数据验证
        DefaultTradeModelChecker defaultTradeModelChecker = new DefaultTradeModelChecker();
        return XPayTradeModelChecker.Delegating.of(limitAmountTradeChecker, defaultTradeModelChecker);
    }


    /**
     * 支付渠道配置
     */
    @Bean
    public XPayIntegrationService xPayIntegrationService(XPayChannelRepository channelRepository,
                                                         ObjectProvider<XPayEventPublisher<Object>> eventPublisherProvider,
                                                         XPaySuccessHandler paySuccessHandler, XPayFailureHandler payFailureHandler,
                                                         WxPayResultResolver wxpayResultResolver,
                                                         CpcnTxRequestExecutor cpcnTxRequestExecutor, CpcnPayResultResolver cpcnPayResultResolver) {
        return DelegatingXPayService.builder()
                // 支付渠道管理仓库
                .channelRepository(channelRepository)
                // 支付事件发布器（可以为空）
                .eventPublisher(eventPublisherProvider.getIfAvailable())
                // 支付服务
                .payServices(
                        // 微信公众号支付或者小程序支付 ( JSAPI 支付)
                        new XPayWxJsApiPayService(XPayChannelEnum.WX_JSAPI.channel(), channelRepository, payFailureHandler, paySuccessHandler, wxpayResultResolver),
                        // 中金支付
                        new XPayCpcnPayService(XPayChannelEnum.CPCN_PAY.channel(), channelRepository, payFailureHandler, paySuccessHandler, cpcnTxRequestExecutor, cpcnPayResultResolver)
                ).build();
    }

    /**
     * 交易单类型拦截处理
     */
    @Bean
    @Primary
    public TradeTypeHandler tradeTypeHandler() {
        return TradeTypeHandler.Delegating.of(
                // 商品交易类型处理
                new GoodsTradeTypeHandler()
        );
    }


}
