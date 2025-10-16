package com.xtechcn.cloud.customer.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarParam {
    /**
     * 主键id
     */
    @Schema(description = "主键id")
    private String id;
    /**
     * 车型
     */
    @NotBlank(message = "车型不能为空")
    @Schema(description = "车型")
    private String name;
    /**
     * 载重（kg）
     */
    @Min(value = 0, message = "载重不能小于0")
    @Schema(description = "载重（kg）")
    private Integer weight;
    /**
     * 长（m）
     */
    @Min(value = 0, message = "长不能小于0")
    @Schema(description = "长（m）")
    private Double length;
    /**
     * 宽（m）
     */
    @Min(value = 0, message = "宽不能小于0")
    @Schema(description = "宽（m）")
    private Double width;
    /**
     * 高（m）
     */
    @Min(value = 0, message = "高不能小于0")
    @Schema(description = "高（m）")
    private Double height;
    /**
     * 车辆类型：1-冷藏车，2-平板车，3-高栏车
     */
    @NotNull(message = "车辆类型不能为空")
    @Schema(description = "车辆类型：1-冷藏车，2-平板车，3-高栏车")
    private Integer type;

    public boolean isAdd() {
        return null == this.id;
    }

}
