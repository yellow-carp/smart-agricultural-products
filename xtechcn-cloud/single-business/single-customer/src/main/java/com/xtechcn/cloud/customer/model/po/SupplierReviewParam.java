package com.xtechcn.cloud.customer.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 供应商审核参数
 * @author Weijixiao
 * @since 2025-09-17 9:48
 */
@Getter
@Setter
public class SupplierReviewParam {

    /**
     * 供应商id
     */
    @Schema(description = "供应商id")
    private String id;
    /**
     * 状态修改
     */
    @Schema(description = "修改状态(1：编辑中、2：待审核、3：已通过、4：已拒绝)")
    private Integer state;
    /**
     * 审核信息
     */
    @Schema(description = "审核信息（驳回原因）")
    private String checkMsg;
}
