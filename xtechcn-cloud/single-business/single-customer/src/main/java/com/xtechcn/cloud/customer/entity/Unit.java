package com.xtechcn.cloud.customer.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.lang.UniqueEntity;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 商品单位
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@Getter
@Setter
@Schema(description = "商品单位")
@TableName(value = "cu_unit")
public class Unit extends Model<Unit> implements UniqueEntity<Unit> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id自增
     */
    @TableId(type = IdType.AUTO)
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
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "操作者", hidden = true)
    private String upsertBy;
    /**
     * 逻辑删除
     */
    @JsonIgnore
    @TableLogic
    @TableField(select = false)
    @Schema(description = "逻辑删除", hidden = true)
    private Boolean isDelete;
    /**
     * 修改时间
     */
    @Schema(description = "修改时间", hidden = true)
    private LocalDateTime updateTime;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间", hidden = true)
    private LocalDateTime createTime;

    @Override
    public boolean isEquals(Unit source) {
        return Objects.equals(this.name, source.name);
    }

    @Override
    public boolean isSelf(Unit source) {
        return Objects.equals(this.id, source.id);
    }
}