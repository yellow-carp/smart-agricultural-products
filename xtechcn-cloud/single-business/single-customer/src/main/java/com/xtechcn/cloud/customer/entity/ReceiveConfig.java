package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.UniqueEntity;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 自动收货配置
 *
 * @author hanjie
 * @since 2025-09-11 17:59:28
 */
@Getter
@Setter
@Schema(description = "自动收货配置")
@TableName(value = "cu_receive_config")
public class ReceiveConfig extends Model<ReceiveConfig> implements UniqueEntity<ReceiveConfig> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 产区id
     */
    @Schema(description = "产区id")
    private String areaOriginId;
    /**
     * 销区id
     */
    @Schema(description = "销区id")
    private String areaSalesId;
    /**
     * 自动收货天数
     */
    @Schema(description = "自动收货天数")
    private Integer day;
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
    public boolean isEquals(ReceiveConfig source) {
        return Objects.equals(this.areaOriginId, source.areaOriginId)
                && Objects.equals(this.areaSalesId, source.areaSalesId);
    }

    @Override
    public boolean isSelf(ReceiveConfig source) {
        return Objects.equals(this.id, source.id);
    }


    public ReceiveConfig withId() {
        this.setId(this.getId());
        return this;
    }

    public ReceiveConfig day(Integer day) {
        this.setDay(day);
        return this;
    }

    public ReceiveConfig areaOriginId(String areaOriginId) {
        this.setAreaOriginId(areaOriginId);
        return this;
    }

    public ReceiveConfig areaSalesId(String areaSalesId) {
        this.setAreaSalesId(areaSalesId);
        return this;
    }
}