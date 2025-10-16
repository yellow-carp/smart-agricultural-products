package com.xtechcn.cloud.payment.core.resolver;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ijpay.core.IJPayHttpResponse;
import com.xtechcn.cloud.payment.core.resolver.decrypt.WxDataDecrypt;
import com.xtechcn.cloud.payment.model.dto.wx.WxPayResult;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.resolver.XPayResultResolver;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 微信支付参数解析
 *
 * @author Alay
 * @since 2023-07-25 13:24
 */
@Component
@RequiredArgsConstructor
public class WxPayResultResolver implements XPayResultResolver {
    private static final String MSG_SUCCESS = "SUCCESS";

    private final WxDataDecrypt wxDataDecrypt;

    /**
     * @see <a href="https://pay.weixin.qq.com/docs/partner/apis/partner-jsapi-payment/payment-notice.html">数据解密</a>
     */
    @Override
    public WxPayResult callbackResolve(HttpServletRequest request) {
        // 数据解密
        String plainText = wxDataDecrypt.callbackDecrypt(request);
        if (StringUtils.isNotEmpty(plainText)) {
            WxPayResult.Data data = IJsonUtil.parseObject(plainText, WxPayResult.Data.class);
            return WxPayResult.builder().way(XPayResult.WAY_NOTIFY).data(data).time(System.currentTimeMillis()).success(true).build();
        }
        return WxPayResult.builder().way(XPayResult.WAY_NOTIFY).success(false).time(System.currentTimeMillis()).build();
    }

    @Override
    public WxPayResult queryResolve(Object parameter) {
        IJPayHttpResponse response = (IJPayHttpResponse) parameter;
        // 查询结果解析
        WxPayResult.Data wxPayData = IJsonUtil.parseObject(response.getBody(), WxPayResult.Data.class);

        return WxPayResult.builder().way(XPayResult.WAY_QUERY)
                .success(MSG_SUCCESS.equals(wxPayData.getTrade_state()))
                .time(System.currentTimeMillis()).data(wxPayData).build();
    }

}
