package com.xtechcn.cloud.payment.model.dto.cpcn;

import com.xtechcn.commom.payment.model.DefaultXPayResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 中金支付返回值
 *
 * @author Alay
 * @since 2023-10-09 13:38
 */
@Getter
@Setter
public class CpcnPayResult extends DefaultXPayResult<CpcnPayData> {
    /**
     * 响应数据明文
     */
    private String plainText;

    public static Builder<CpcnPayData, CpcnPayResult> builder() {
        return new DefaultXPayResult.Builder<>(CpcnPayResult::new);
    }

    @Override
    public String getMessage() {
        Object responseMessage = this.data().attribute("responseMessage");
        Object message = this.data().attribute("message");
        return null != responseMessage ? responseMessage.toString() : message.toString();
    }

    @Override
    public String outTradeNo() {
        if (this.data() == null) return null;
        return this.data().getTradeNo();
    }

    public CpcnPayResult plainText(String plainText) {
        this.plainText = plainText;
        return this;
    }

}
