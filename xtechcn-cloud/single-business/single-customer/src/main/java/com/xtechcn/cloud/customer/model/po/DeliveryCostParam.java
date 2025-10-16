package com.xtechcn.cloud.customer.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class DeliveryCostParam {
    /**
     * 主键id
     */
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 车辆id
     */
    @NotBlank(message = "车辆id不能为空")
    @Schema(description = "车辆id")
    private String carId;
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
     * 单趟固定成本
     */
    @Min(value = 0, message = "单趟固定成本不能小于0")
    @Schema(description = "单趟固定成本")
    private MoneyPenny cost;
    /**
     * 区域距离（km）
     */
    @Min(value = 0, message = "区域距离不能小于0")
    @Schema(description = "区域距离（km）")
    private Double regionDistance;


    public Boolean isAdd(){
        return null==this.id;
    }
}
