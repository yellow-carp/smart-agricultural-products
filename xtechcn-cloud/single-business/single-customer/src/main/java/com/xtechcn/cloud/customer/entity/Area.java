package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.xtechcn.cloud.customer.model.dto.RegionDto;
import com.xtechcn.cloud.customer.typehandler.RegionTypeHandler;
import com.xtechcn.common.core.lang.UniqueEntity;
import com.xtechcn.common.mybatis.typehandler.Str2ListTypeHandler;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import com.xtechcn.common.serialization.lang.JsonObj;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 区域管理
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@Getter
@Setter
@Schema(description = "区域管理")
@TableName(value = "cu_area", autoResultMap = true)
public class Area extends Model<Area> implements UniqueEntity<Area> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 区域名称
     */
    @Schema(description = "区域名称")
    private String name;
    /**
     * 区域类型：1-产区，2-销区
     */
    @Schema(description = "区域类型：1-产区，2-销区")
    private Integer type;
    /**
     * 物流模式：1-整车，2-整车+拼车
     */
    @Schema(description = "物流模式：1-整车，2-整车+拼车")
    private Integer deliveryModel;
    /**
     * 启用禁用
     */
    @Schema(description = "启用禁用")
    private Boolean enabled;
    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;
    /**
     * 行政区域全路径
     */
    @Schema(description = "行政区域全路径")
    private String regionFullPath;
    /**
     * 行政区域全名
     */
    @Schema(description = "行政区域全名")
    private String regionFullName;
    /**
     * 行政区域编码列表
     */
    @Schema(description = "行政区域编码列表")
    @TableField(typeHandler = RegionTypeHandler.class)
    private List<RegionDto> regionCodeList;
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
    public boolean isEquals(Area source) {
        return Objects.equals(this.regionCodeList,source.regionCodeList) && Objects.equals(this.type,source.type);
    }

    @Override
    public boolean isSelf(Area source) {
        return Objects.equals(this.id, source.id);
    }

    public Area withId() {
        this.setId(this.getId());
        return this;
    }

    public Area enable(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }
}