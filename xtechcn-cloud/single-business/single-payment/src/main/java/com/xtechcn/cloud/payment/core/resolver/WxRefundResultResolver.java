package com.xtechcn.cloud.payment.core.resolver;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xtechcn.cloud.payment.core.resolver.decrypt.WxDataDecrypt;
import com.xtechcn.cloud.payment.model.dto.wx.WxRefundResult;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.resolver.XPayResultResolver;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 退款解析
 *
 * @author Alay
 * @see <a href="https://pay.weixin.qq.com/docs/merchant/apis/mini-program-payment/refund-result-notice.html">官方文档</a>
 * @since 2024-06-21 13:12
 */
@Component
@RequiredArgsConstructor
public class WxRefundResultResolver implements XPayResultResolver {
    private final WxDataDecrypt wxDataDecrypt;

    @Override
    public WxRefundResult callbackResolve(HttpServletRequest request) {
        // 数据解密
        String plainText = wxDataDecrypt.callbackDecrypt(request);
        // 构建返回值
        if (StringUtils.isNotEmpty(plainText)) {
            WxRefundResult.RefundData refundData = IJsonUtil.parseObject(plainText, WxRefundResult.RefundData.class);
            return WxRefundResult.builder().way(XPayResult.WAY_NOTIFY).data(refundData).time(System.currentTimeMillis()).success(true).build();
        }
        return WxRefundResult.builder().way(XPayResult.WAY_NOTIFY).success(false).time(System.currentTimeMillis()).build();
    }

    @Override
    public XPayResult<?> queryResolve(Object parameter) {
        return null;
    }

}
