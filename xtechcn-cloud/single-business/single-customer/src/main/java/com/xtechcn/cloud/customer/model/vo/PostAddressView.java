package com.xtechcn.cloud.customer.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostAddressView {
    /**
     * 主键id
     */
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 采购商id
     */
    @Schema(description = "采购商id", hidden = true)
    private String userId;
    /**
     * 行政区域编码
     */
    @Schema(description = "行政区域编码")
    private String regionCode;
    /**
     * 行政区域全路径
     */
    @Schema(description = "行政区域全路径")
    private String regionFullPath;
    /**
     * 行政区域全名
     */
    @Schema(description = "行政区域全名")
    private String regionFullName;
    /**
     * 邮政编码
     */
    @Schema(description = "邮政编码")
    private String zip;
    /**
     * 详细地址
     */
    @Schema(description = "详细地址")
    private String detail;
    /**
     * 公司名称
     */
    @Schema(description = "公司名称")
    private String comName;
    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名")
    private String contact;
    /**
     * 收货人电话
     */
    @Schema(description = "收货人电话")
    private String contactTel;
    /**
     * 是否为默认地址
     */
    @Schema(description = "是否为默认地址")
    private Boolean isDefault;
}
