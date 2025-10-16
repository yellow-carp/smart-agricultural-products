package com.xtechcn.cloud.payment.model.dto.cpcn;

import com.xtechcn.common.core.lang.XAttributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 中金支付数据
 *
 * @author Alay
 * @since 2023-10-09 13:54
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpcnPayData implements Serializable, XAttributes<String> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 支付原始数据
     */
    @Getter
    private Object original;
    /**
     * 原交易单号
     */
    @Getter
    private String tradeNo;
    /**
     * 中金交易流水号 txSN
     */
    @Getter
    private String txSn;
    /**
     * 解密后的数据文本内容
     */
    @Getter
    private String plainText;
    /**
     * 响应码
     */
    @Getter
    private String txCode;
    /**
     * 状态
     */
    @Getter
    private String status;
    /**
     * 是否成功
     */
    @Getter
    private boolean success;
    /**
     * 支付数据转Map
     */
    private Map<String, Object> attributes;

    @Override
    public CpcnPayData addAttribute(String key, Object value) {
        if (null == this.attributes) this.attributes = new ConcurrentHashMap<>();
        this.attributes.put(key, value);
        return this;
    }

    public Object original() {
        return original;
    }

}
