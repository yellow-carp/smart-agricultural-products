package com.xtechcn.cloud.payment.model.dto.wx;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 微信平台证书数据
 *
 * @author Alay
 * @since 2023-06-11 14:33
 */
@Getter
@Setter
public class EffectiveResultDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String effective_time;
    private String expire_time;
    private String serial_no;
    private Certificate encrypt_certificate;


    @Getter
    @Setter
    public static class Certificate {
        private String algorithm;
        private String associated_data;
        private String ciphertext;
        private String nonce;
    }


}
