package com.xtechcn.cloud.customer.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveConfigView {
    /**
     * 主键id
     */
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 产区id
     */
    @Schema(description = "产区id")
    private String areaOriginId;
    /**
     * 产区名称
     */
    @Schema(description = "产区名称")
    private String areaOriginName;
    /**
     * 销区id
     */
    @Schema(description = "销区id")
    private String areaSalesId;
    /**
     * 销区名称
     */
    @Schema(description = "销区名称")
    private String areaSalesName;
    /**
     * 自动收货天数
     */
    @Schema(description = "自动收货天数")
    private Integer day;
}
