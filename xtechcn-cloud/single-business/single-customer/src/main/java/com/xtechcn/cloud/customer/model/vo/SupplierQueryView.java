package com.xtechcn.cloud.customer.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Weijixiao
 * @since 2025-09-17 9:40
 */
@Getter
@Setter
public class SupplierQueryView {
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id")
    private String id;
    /**
     * 统一社会信用代码
     */
    @Schema(description = "统一社会信用代码")
    private String usci;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称")
    private String name;
    /**
     * 法人
     */
    @Schema(description = "法人")
    private String juridicalUser;
    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    private String contactName;
    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话")
    private String contactPhone;
    /**
     * 所属产区id
     */
    @Schema(description = "所属产区id")
    private String areaId;
    /**
     * 营业执照
     */
    @Schema(description = "营业执照")
    private String license;
}
