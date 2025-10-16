package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.UniqueEntity;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 包装形式
 *
 * @author hanjie
 * @since 2025-09-11 17:59:28
 */
@Getter
@Setter
@Schema(description = "包装形式")
@TableName(value = "cu_packaging")
public class Packaging extends Model<Packaging> implements UniqueEntity<Packaging> {
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
     * 长（cm）
     */
    @Schema(description = "长（cm）")
    private Double length;
    /**
     * 宽（cm）
     */
    @Schema(description = "宽（cm）")
    private Double width;
    /**
     * 高（cm）
     */
    @Schema(description = "高（cm）")
    private Double height;
    /**
     * 是否禁用(0:禁用,1：启用)
     */
    @Schema(description = "是否禁用(0:禁用,1：启用)")
    private Boolean enabled;
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
    public boolean isEquals(Packaging source) {
        return Objects.equals(this.name,source.name)
                && Objects.equals(this.length,source.length)
                && Objects.equals(this.width,source.width)
                && Objects.equals(this.height,source.height);
    }

    @Override
    public boolean isSelf(Packaging source) {
        return Objects.equals(this.id,source.id);
    }

    public Packaging withId() {
        this.setId(this.id);
        return this;
    }
    public Packaging enable(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }
}