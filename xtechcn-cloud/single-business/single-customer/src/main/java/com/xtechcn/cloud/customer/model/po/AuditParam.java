package com.xtechcn.cloud.customer.model.po;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 审核参数
 *
 * @author hanjie
 * @since 2025-9-17 15:56:08
 */
@Getter
@Setter
public class AuditParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @NotBlank(message = "id不能空")
    private String id;
    /**
     * 审核消息(拒审时必填)
     */
    private String auditMsg;
    /**
     * 通过
     */
    @NotNull(message = "审核结论必填")
    private Boolean passed;

}
