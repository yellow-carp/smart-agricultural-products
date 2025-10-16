package com.xtechcn.cloud.payment.core.resolver.decrypt;

import cn.hutool.core.util.StrUtil;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.resolver.XPayDataDecrypt;
import com.xtechcn.common.cpcn.constants.CpcnStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import payment.api.notice.NoticeRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 中金支付数据解密
 *
 * @author Alay
 * @since 2024-06-21 10:33
 */
@Component
@RequiredArgsConstructor
public class CpcnDataDecrypt implements XPayDataDecrypt {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public NoticeRequest callbackDecrypt(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Iterator<String> ite = request.getHeaderNames().asIterator();
        while (ite.hasNext()) {
            String key = ite.next();
            headers.put(key, request.getHeader(key));
        }
        logger.info("请求头信息：{}", headers);
        // 1 获得参数message和signature
        String message = request.getParameter("message");
        String signature = request.getParameter("signature");
        String isDgEnv = request.getParameter("isDgEnv");
        String digitalEnvelope = request.getParameter("digitalEnvelope");
        String signAlgorithm = request.getParameter("signAlgorithm");
        String signSN = request.getParameter("signSN");
        String encryptSN = request.getParameter("encryptSN");
        // 2 生成交易结果对象
        NoticeRequest noticeRequest;
        try {
            if (CpcnStatus.YES.equals(isDgEnv)) {
                // 数字信封解密验签
                noticeRequest = new NoticeRequest(message, signature, digitalEnvelope, signSN, encryptSN, signAlgorithm);
            } else {
                // 非数字信封解密验签
                if (StrUtil.isNotEmpty(signSN)) {
                    message = message + "," + signSN + "," + signAlgorithm;
                }
                noticeRequest = new NoticeRequest(message, signature);
            }
            return noticeRequest;
        } catch (Exception ex) {
            throw new XPayException(ex);
        }
    }
}
