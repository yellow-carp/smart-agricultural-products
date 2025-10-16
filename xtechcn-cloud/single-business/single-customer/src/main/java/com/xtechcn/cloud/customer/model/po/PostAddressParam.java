package com.xtechcn.cloud.customer.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostAddressParam {
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
     * 行政区域编码
     */
    @NotBlank(message = "行政区域编码不能为空")
    @Schema(description = "行政区域编码")
    private String regionCode;
    /**
     * 行政区域全路径
     */
    @NotBlank(message = "行政区域全路径不能为空")
    @Schema(description = "行政区域全路径")
    private String regionFullPath;
    /**
     * 行政区域全名
     */
    @NotBlank(message = "行政区域全名不能为空")
    @Schema(description = "行政区域全名")
    private String regionFullName;
    /**
     * 邮政编码
     */
    @NotBlank(message = "邮政编码不能为空")
    @Pattern(
            regexp = "^[0-9]{6}$",  // 正则：仅允许6位数字
            message = "邮政编码格式错误，需为6位数字（如：100000）"
    )
    @Schema(description = "邮政编码")
    private String zip;
    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    @Schema(description = "详细地址")
    private String detail;
    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空")
    @Schema(description = "公司名称")
    private String comName;
    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名")
    private String contact;
    /**
     * 收货人电话
     */
    @NotBlank(message = "电话号码不能为空")
    @Pattern(
            regexp = "^1[3-9]\\d{9}$",  // 中国大陆手机号正则：1开头，第二位3-9，后面9位数字
            message = "电话号码格式不正确，请输入11位有效手机号"
    )
    @Schema(description = "收货人电话")
    private String contactTel;
    /**
     * 是否为默认地址
     */
    @Schema(description = "是否为默认地址")
    private Boolean isDefault;
    public boolean isAdd() {
        return null == this.id;
    }
}
