package com.xtechcn.cloud.payment.core.resolver.decrypt;

import cn.hutool.core.io.IoUtil;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.xtechcn.cloud.payment.core.wx.WxConfigTool;
import com.xtechcn.commom.payment.channel.XPayChannel;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.conf.WxPayConfModel;
import com.xtechcn.commom.payment.resolver.XPayDataDecrypt;
import com.xtechcn.common.core.utils.IParamUtil;
import com.xtechcn.common.core.utils.IStrPool;
import com.xtechcn.common.web.header.HeaderContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 微信支付解谜操作
 *
 * @author Alay
 * @since 2024-06-21 10:28
 */
@Component
@RequiredArgsConstructor
public class WxDataDecrypt implements XPayDataDecrypt {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    // 平台证书解析的系列号/请求头系列号
    private static final String PLATFORM_NONCE = "Wechatpay-Nonce";
    private static final String PLATFORM_SERIAL = "Wechatpay-Serial";
    private static final String PLATFORM_SIGNATURE = "Wechatpay-Signature";
    private static final String PLATFORM_TIMESTAMP = "Wechatpay-Timestamp";

    private final XPayChannelRepository xPayChannelRepository;


    @Override
    public String callbackDecrypt(HttpServletRequest request) {
        // 所属应用
        String ofClient = HeaderContextHolder.ofClient();
        Integer appType = HeaderContextHolder.appType();
        // 支付渠道
        String channel = request.getHeader("channel");
        if (channel.contains(IStrPool.DASHED)) {
            channel = channel.replace(IStrPool.DASHED, IStrPool.CARET);
        }
        // 2_3_3^1
        String channelKey = String.format(XPayChannel.THREE_KEY_FORMAT, appType, ofClient, IParamUtil.ofStrValue(channel, "3^1"));

        // 支付渠道查询
        XPayChannel xPayChannel = xPayChannelRepository.findOne(channelKey);
        WxPayConfModel wxConfig = xPayChannelRepository.transform(xPayChannel, WxPayConfModel.class);
        try {
            Map<String, String> headers = new HashMap<>();
            Iterator<String> ite = request.getHeaderNames().asIterator();
            while (ite.hasNext()) {
                String key = ite.next();
                headers.put(key, request.getHeader(key));
            }
            logger.info("wx pay notify headers：{}", headers);
            // 读取请求信息
            String body = HttpKit.readData(request);

            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            String plainText = WxPayKit.verifyNotify(
                    // 平台证书解析的系列号/请求头系列号
                    headers.get(PLATFORM_SERIAL),
                    // body
                    body,
                    // signature
                    headers.get(PLATFORM_SIGNATURE),
                    // nonce
                    headers.get(PLATFORM_NONCE),
                    // timestamp
                    headers.get(PLATFORM_TIMESTAMP),
                    wxConfig.getApiKey3(),
                    IoUtil.toStream(WxConfigTool.platformCert(wxConfig).getBytes())
            );

            logger.info("wx pay notify data decrypt context {}", plainText);
            return plainText;
        } catch (Exception ex) {
            logger.error("wx pay notify data decrypt plainText failed, message: [{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

}
