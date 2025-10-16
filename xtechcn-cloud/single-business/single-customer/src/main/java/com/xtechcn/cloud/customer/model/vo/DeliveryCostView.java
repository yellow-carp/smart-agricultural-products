package com.xtechcn.cloud.customer.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DeliveryCostView {
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 车辆id
     */
    @Schema(description = "车辆id")
    private String carId;
    /**
     * 车辆名称
     */
    @Schema(description = "车辆名称")
    private String carName;
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
     * 单趟固定成本
     */
    @Schema(description = "单趟固定成本")
    private MoneyPenny cost;
    /**
     * 区域距离（km）
     */
    @Schema(description = "区域距离（km）")
    private Double regionDistance;
    /**
     * 单位成本（元/车·公里）
     */
    @Schema(description = "单位成本（元/车·公里）")
    private MoneyPenny unitCost;
}
