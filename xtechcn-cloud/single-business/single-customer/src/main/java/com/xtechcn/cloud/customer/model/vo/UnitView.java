package com.xtechcn.cloud.customer.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class UnitView {
    /**
     * 主键id自增
     */
    @Schema(description = "主键id自增", hidden = true)
    private Long id;
    /**
     * 单位名称
     */
    @Schema(description = "单位名称")
    private String name;
    /**
     * 提成金额
     */
    @Schema(description = "提成金额")
    private MoneyPenny commAmount;
    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;
    /**
     * 操作者
     */
    @Schema(description = "操作者", hidden = true)
    private String upsertBy;
}
