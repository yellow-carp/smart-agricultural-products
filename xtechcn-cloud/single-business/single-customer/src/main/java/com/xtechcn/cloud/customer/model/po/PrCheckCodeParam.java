package com.xtechcn.cloud.customer.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Hanjie
 * @since 2025-09-18 12:00
 */
@Getter
@Setter
public class PrCheckCodeParam implements Serializable {

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    @NotNull(message = "验证码必填")
    private String code;
}
