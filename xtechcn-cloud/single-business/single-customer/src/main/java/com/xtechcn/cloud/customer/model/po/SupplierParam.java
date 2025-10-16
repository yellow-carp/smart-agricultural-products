package com.xtechcn.cloud.customer.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author chengzuo
 * @since 2024-04-19 09:01
 */
@Getter
@Setter
public class SupplierParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Schema(description = "主键")
    private String id;
    /**
     * 统一社会信用代码
     */
    @NotBlank(message = "统一社会信用代码必填")
    @Schema(description = "统一社会信用代码")
    private String usci;
    /**
     * 商户名称必填
     */
    @NotBlank(message = "商户名称必填")
    @Schema(description = "商户名称必填")
    private String name;

    /**
     * 所属产区
     */
    @Schema(description = "所属产区")
    private String areaId;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名必填")
    @Schema(description = "联系人姓名")
    private String contactName;

    /**
     * 联系人邮箱
     */
    @Email
    @Schema(description = "联系人邮箱")
    private String contactEmail;

    /**
     * 联系人电话
     */
    @NotBlank(message = "联系人电话必填")
    @Schema(description = "联系人电话")
    private String contactPhone;

    /**
     * 营业执照
     */
    @NotBlank(message = "营业执照必填")
    @Schema(description = "营业执照")
    private String license;
    /**
     * 法人
     */
    @NotBlank(message = "法人必填")
    @Schema(description = "法人")
    private String juridicalUser;
    /**
     * 证件生效日
     */
    @Schema(description = "证件生效日")
    private LocalDate certStartTime;

    /**
     * 证件结束日
     */
    @Schema(description = "证件结束日")
    private LocalDate certEndTime;

    /**
     * 管理类型(0:经办人，1：法人)
     */
    @Schema(description = "管理类型(0:经办人，1：法人)")
    private Integer manageType;

    /**
     * 注册地址
     */
    @Schema(description = "注册地址")
    private String address;
    /**
     * 店铺图片
     */
    @Schema(description = "店铺图片（供应商）")
    private String shopImage;
    /**
     * openId
     */
    @Schema(description = "openId（供应商）")
    private String openId;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private String userId;

    public boolean isAdd() {
        return null == this.id;
    }
}
