package com.xtechcn.cloud.payment.core.wx;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.AesUtil;
import com.ijpay.core.kit.PayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.OtherApiEnum;
import com.xtechcn.cloud.payment.model.dto.wx.EffectiveResultDto;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.conf.WxPayConfModel;
import com.xtechcn.common.core.exceptions.IRuntimeException;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * 微信配置工具
 *
 * @author Alay
 * @since 2023-08-07 14:47
 */
@Component
@RequiredArgsConstructor
public class WxConfigTool implements ApplicationContextAware {

    private static Logger logger;
    private static StringRedisTemplate stringRedisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        stringRedisTemplate = context.getBean(StringRedisTemplate.class);
        logger = LoggerFactory.getLogger(this.getClass());
    }


    /**
     * 获取平台证书系列
     */
    public static String serialNo(WxPayConfModel config) {
        // 证书路径获取证书序列号
        X509Certificate certificate = PayKit.getCertificate(IoUtil.toStream(config.getCertText().getBytes()));
        if (null != certificate) {
            String serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
            // 提前两天检查证书是否有效
            boolean isValid = PayKit.checkCertificateIsValid(certificate, config.getMchId(), -2);
            logger.warn("证书是否可用{} 证书有效期为{}", isValid, DateUtil.format(certificate.getNotAfter(), DatePattern.NORM_DATETIME_PATTERN));
            return serialNo;
        }
        throw new XPayException("Invalided wx cert.pem");
    }

    /**
     * 查询证书列表
     */
    public static String platformCert(WxPayConfModel config) {
        String platformCertKey = config.getPlatformCert();
        platformCertKey = Optional.ofNullable(platformCertKey).orElse(config.getChannelKey());
        // 缓冲中获取
        String publicKey = stringRedisTemplate.opsForValue().get(platformCertKey);
        // 缓存没有，查询微信平台
        if (null == publicKey) {
            List<EffectiveResultDto> effectiveResults = queryPlatformCert(config);
            if (ICollUtil.isEmpty(effectiveResults)) throw new IRuntimeException("没有查询到平台证书");

            EffectiveResultDto effective = effectiveResults.getFirst();
            // 目前 size = 1 所以只有一个
            publicKey = parsePublicKey(config, effective);

            stringRedisTemplate.opsForValue().set(platformCertKey, publicKey, Duration.ofSeconds(1800));
        }
        return publicKey;
    }

    private static List<EffectiveResultDto> queryPlatformCert(WxPayConfModel config) {
        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    OtherApiEnum.GET_CERTIFICATES.toString(),
                    config.getMchId(),
                    serialNo(config),
                    null,
                    PayKit.getPrivateKeyByKeyContent(config.getKeyText(), AuthTypeEnum.RSA.getCode()),
                    "");
            R<List<EffectiveResultDto>> wxResponse = IJsonUtil.parseObject(response.getBody(), new TypeReference<R<List<EffectiveResultDto>>>() {
            });
            return wxResponse.getData();
        } catch (Exception e) {
            throw new IRuntimeException(e.getMessage());
        }
    }

    /**
     * 平台证书密文解密
     */
    private static String parsePublicKey(WxPayConfModel config, EffectiveResultDto effective) {
        try {
            AesUtil aesUtil = new AesUtil(config.getApiKey3().getBytes(StandardCharsets.UTF_8));
            // 平台证书密文解密
            // encrypt_certificate 中的  associated_data nonce  ciphertext
            return aesUtil.decryptToString(
                    effective.getEncrypt_certificate().getAssociated_data().getBytes(StandardCharsets.UTF_8),
                    effective.getEncrypt_certificate().getNonce().getBytes(StandardCharsets.UTF_8),
                    effective.getEncrypt_certificate().getCiphertext()
            );
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
            throw new IRuntimeException(e.getMessage());
        }
    }


}
