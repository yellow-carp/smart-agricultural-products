package com.xtechcn.cloud.customer.model.po;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class UnitPageParam {
    /**
     * 单位名称
     */
    @TableField(condition = SqlCondition.LIKE)
    @Schema(description = "单位名称")
    private String name;
    /**
     * 提成金额
     */
    @Schema(description = "提成金额")
    @Min(value = 0, message = "提成金额不能小于0")
    private MoneyPenny commAmount;
}
