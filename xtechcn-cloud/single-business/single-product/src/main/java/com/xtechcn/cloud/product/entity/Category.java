package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.UniqueEntity;
import com.xtechcn.common.core.lang.tree.ITreeNode;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 商品分类
 *
 * @author hanjie
 * @since 2025-09-16 09:45:56
 */
@Getter
@Setter
@Schema(description = "商品分类")
@TableName(value = "pd_category")
public class Category extends Model<Category> implements UniqueEntity<Category>, ITreeNode<Category> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 父节点id
     */
    @Schema(description = "父节点id")
    private String pid;
    /**
     * 层级级别（顶级为1,子节点递增）
     */
    @Schema(description = "层级级别（顶级为1,子节点递增）")
    private Integer level;
    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String name;
    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;
    /**
     * 背景图
     */
    @Schema(description = "背景图")
    private String bgPic;
    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;
    /**
     * 全路径
     */
    @Schema(description = "全路径")
    private String fullPath;
    /**
     * 孩子数
     */
    @Schema(description = "孩子数")
    private Integer countChi;
    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean enabled;
    /**
     * 说明描述
     */
    @Schema(description = "说明描述")
    private String notes;
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
     * 操作时间
     */
    @Schema(description = "操作时间", hidden = true)
    private LocalDateTime upsertTime;

    public boolean getHasChildren() {
        return null != this.countChi && this.countChi > 0;
    }

    public void plusChild() {
        this.countChi++;
    }

    public void minusChild() {
        this.countChi--;
    }

    @Override
    public boolean isEquals(Category source) {
        return Objects.equals(pid, source.pid) && Objects.equals(this.name, source.name);
    }

    @Override
    public boolean isSelf(Category source) {
        return Objects.equals(this.id, source.id);
    }

    @Override
    public Serializable code() {
        return null;
    }

    @Override
    public Serializable parentCode() {
        return null;
    }

    @Override
    public Integer level() {
        return this.level;
    }
}