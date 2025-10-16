package com.xtechcn.cloud.customer.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Hanjie
 * @since 2025-09-17 14:49
 */
@Getter
@Setter
public class SupplierInfoQuery implements Serializable {

    /**
     * openID
     */
    @Schema(description = "openID")
    @NotNull(message = "openID不能为空")
    private String openId;

}
