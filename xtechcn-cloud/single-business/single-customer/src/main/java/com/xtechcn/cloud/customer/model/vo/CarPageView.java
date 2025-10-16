package com.xtechcn.cloud.customer.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CarPageView {
    /**
     * 主键id
     */
    @Schema(description = "主键id", hidden=true)
    private String id;
    /**
     * 车型
     */
    @Schema(description = "车型")
    private String name;
    /**
     * 长（m）
     */
    @Schema(description = "长（m）")
    private Double length;
    /**
     * 宽（m）
     */
    @Schema(description = "宽（m）")
    private Double width;
    /**
     * 高（m）
     */
    @Schema(description = "高（m）")
    private Double height;
    /**
     * 载重（kg）
     */
    @Schema(description = "载重（kg）")
    private Integer weight;
    /**
     * 启用禁用
     */
    @Schema(description = "启用禁用")
    private Boolean enabled;
    /**
     * 容积
     */
    @Schema(description = "容积（立方）")
    private Double volume;
    /**
     * 最大货物件数
     */
    @Schema(description = "最大货物件数")
    private Integer maxNum;
    /**
     * 最小货物件数
     */
    @Schema(description = "最小货物件数")
    private Integer minNum;
    /**
     * 车辆类型：1-冷藏车，2-平板车，3-高栏车
     */
    @Schema(description = "车辆类型：1-冷藏车，2-平板车，3-高栏车")
    private Integer type;

}
