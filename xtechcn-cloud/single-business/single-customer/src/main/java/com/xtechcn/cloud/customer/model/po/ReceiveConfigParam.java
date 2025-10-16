package com.xtechcn.cloud.customer.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ReceiveConfigParam {
    /**
     * 主键id
     */
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 产区id
     */
    @NotBlank(message = "产区id不能为空")
    @Schema(description = "产区id")
    private String areaOriginId;
    /**
     * 销区id
     */
    @NotBlank(message = "销区id不能为空")
    @Schema(description = "销区id")
    private String areaSalesId;
    /**
     * 自动收货天数
     */
    @Min(value = 0, message = "自动收货天数不能小于0")
    @Schema(description = "自动收货天数")
    private Integer day;

    public boolean isAdd() {
        return null == this.id;
    }
}
