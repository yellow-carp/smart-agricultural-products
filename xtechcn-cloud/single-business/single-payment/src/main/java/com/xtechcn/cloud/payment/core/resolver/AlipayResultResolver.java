package com.xtechcn.cloud.payment.core.resolver;

import cn.hutool.core.bean.BeanUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ijpay.alipay.AliPayApiConfig;
import com.xtechcn.cloud.payment.model.dto.ali.AliPayResult;
import com.xtechcn.commom.payment.channel.XPayChannel;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.resolver.XPayResultResolver;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付宝支付请求参数解析
 *
 * @author Alay
 * @since 2023-07-25 13:15
 */
@RequiredArgsConstructor
public class AlipayResultResolver implements XPayResultResolver {

    protected static final String SUCCESS_STATUS = "TRADE_SUCCESS";

    private final XPayChannelRepository xPayChannelRepository;

    @Override
    public AliPayResult callbackResolve(HttpServletRequest request) {
        // 参数解析
        Map<String, String> params = request.getParameterMap().entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0]));

        // 参数转换为对象
        AliPayResult.Data aliPayData = map2Data(params);

        // 注意此处需要给微信传递的参数,必须保证一致性
        String channelKey = aliPayData.getPassbackParams();
        // 配置信息获取
        XPayChannel config = xPayChannelRepository.findOne(channelKey);
        AliPayApiConfig aliConfig = xPayChannelRepository.transform(config, AliPayApiConfig.class);

        try {
            // 验签情况
            boolean signVerified = AlipaySignature.rsaCheckV1(params, aliConfig.getAliPayPublicKey(),
                    aliConfig.getCharset(), aliConfig.getSignType());

        } catch (AlipayApiException ex) {
            throw new XPayException(ex);
        }
        // 返回值
        return AliPayResult.builder()
                .data(aliPayData)
                .success(true)
                // 支付成功时间，去当前时间
                .time(System.currentTimeMillis())
                .way(XPayResult.WAY_NOTIFY).build();
    }


    @Override
    public AliPayResult queryResolve(Object parameter) {
        // 响应结果转换为支付结果
        AlipayTradeQueryResponse response = (AlipayTradeQueryResponse) parameter;
        AliPayResult.Data aliPayData = jsonStr2Data(response.getBody());
        String tradeStatus = response.getTradeStatus();

        // 返回值构建
        return AliPayResult.builder().way(XPayResult.WAY_QUERY)
                // 是否支付成功
                .success(SUCCESS_STATUS.equals(tradeStatus))
                // 支付成功时间，取当前时间
                .time(System.currentTimeMillis())
                .data(aliPayData)
                .build();
    }

    private static AliPayResult.Data map2Data(Map<String, String> map) {
        AliPayResult.Data aliPayResultData = new AliPayResult.Data();
        BeanUtil.copyProperties(map, aliPayResultData);
        return aliPayResultData;
    }

    private static AliPayResult.Data jsonStr2Data(String jsonStr) {
        return IJsonUtil.parseObject(jsonStr, AliPayResult.Data.class);
    }

}
