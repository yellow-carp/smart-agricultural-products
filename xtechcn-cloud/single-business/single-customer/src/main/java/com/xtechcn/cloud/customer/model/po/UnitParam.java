package com.xtechcn.cloud.customer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class UnitParam {
    /**
     * 主键id自增
     */
    @Schema(description = "主键id自增", hidden = true)
    private Long id;
    /**
     * 单位名称
     */
    @NotBlank(message = "单位名称不能为空")
    @Schema(description = "单位名称")
    private String name;
    /**
     * 提成金额
     */
    @Min(value = 0, message = "提成金额不能小于0")
    @Schema(description = "提成金额")
    private MoneyPenny commAmount;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;

    public boolean isAdd() {
        return null == this.id;
    }
}
