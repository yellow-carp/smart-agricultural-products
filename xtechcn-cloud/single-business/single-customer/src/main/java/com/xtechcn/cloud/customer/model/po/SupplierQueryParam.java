package com.xtechcn.cloud.customer.model.po;

import com.xtechcn.common.mybatis.wrapper.SqlCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 供应商查询参数（管理端）
 *
 * @author Weijixiao
 * @since 2025-09-17 9:27
 */
@Getter
@Setter
public class SupplierQueryParam {

    /**
     * 编码
     */
    @Schema(description = "编码")
    @SqlCondition(type = SqlCondition.Type.LIKE)
    private String id;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称")
    @SqlCondition(type = SqlCondition.Type.LIKE)
    private String name;
    /**
     * 所属产区id
     */
    @Schema(description = "所属产区id")
    @SqlCondition(type = SqlCondition.Type.LIKE)
    private String areaId;
    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    @SqlCondition(type = SqlCondition.Type.LIKE)
    private String contactName;
    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话")
    @SqlCondition(type = SqlCondition.Type.LIKE)
    private String contactPhone;
    /**
     * 申请状态(1：编辑中、2：待审核、3：已通过、4：已拒绝)
     */
    @Schema(description = "申请状态(1：编辑中、2：待审核、3：已通过、4：已拒绝)")
    private Integer state;
}
