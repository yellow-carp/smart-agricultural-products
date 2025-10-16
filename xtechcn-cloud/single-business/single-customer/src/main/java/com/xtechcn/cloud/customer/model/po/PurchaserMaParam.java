package com.xtechcn.cloud.customer.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xtechcn.common.mybatis.typehandler.Str2SetTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
public class PurchaserMaParam {
    /**
     * 主键id
     */
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 采购商id
     */
    @NotBlank(message = "采购商id不能为空")
    @Schema(description = "采购商id", hidden = true)
    private String userId;
    /**
     * 统一社会信用代码
     */
    @NotBlank(message = "统一社会信用代码不能为空")
    @Schema(description = "统一社会信用代码")
    private String usci;
    /**
     * 采购商名称
     */
    @NotBlank(message = "采购商名称不能为空")
    @Schema(description = "采购商名称")
    private String name;
    /**
     * 法人
     */
    @NotBlank(message = "法人不能为空")
    @Schema(description = "法人")
    private String juridicalUser;
    /**
     * 所属产区id
     */
    @NotBlank(message = "所属产区id不能为空")
    @Schema(description = "所属产区id")
    private String areaId;
    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    @Schema(description = "联系人姓名")
    private String contactName;
    /**
     * 联系人电话
     */
    @NotBlank(message = "联系人电话不能为空")
    @Schema(description = "联系人电话")
    private String contactPhone;
    /**
     * 营业执照
     */
    @NotBlank(message = "营业执照不能为空")
    @Schema(description = "营业执照")
    private String license;

    public Boolean isAdd(){
        return null == this.getId();
    }
}
