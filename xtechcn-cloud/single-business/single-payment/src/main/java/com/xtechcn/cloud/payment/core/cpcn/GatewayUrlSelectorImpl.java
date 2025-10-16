package com.xtechcn.cloud.payment.core.cpcn;

import com.xtechcn.commom.payment.config.properties.XPayConfigProperties;
import com.xtechcn.common.cpcn.api.GatewayUrlSelector;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import payment.api.system.PaymentEnvironment;

/**
 * 中金网关地址选择器
 *
 * @author 唐杰
 * @since 2024-09-25 16:00
 */
@Component
@RequiredArgsConstructor
public class GatewayUrlSelectorImpl implements GatewayUrlSelector {
    /**
     * 影印件上传地址
     */
    @Value("${cpcn.gateway.fileUrl}")
    private String fileUrl = "https://www.china-clearing.com/Gateway4File/InterfaceII";

    @Override
    public String urlSelect(String txCode) {
        if (txCode.equals("4600")) {
            // return "https://test.cpcn.com.cn/Gateway4File/InterfaceII";
            // return "https://www.china-clearing.com/Gateway4File/InterfaceII";
            return fileUrl;
        }
        return PaymentEnvironment.txURL;
    }
}
