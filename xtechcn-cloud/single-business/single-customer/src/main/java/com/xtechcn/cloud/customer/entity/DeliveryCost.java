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
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 物流成本
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@Getter
@Setter
@Schema(description = "物流成本")
@TableName(value = "cu_delivery_cost")
public class DeliveryCost extends Model<DeliveryCost> implements UniqueEntity<DeliveryCost> {
    @Serial
    private static final long serialVersionUID = 1L;
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
    public boolean isEquals(DeliveryCost source) {
        return Objects.equals(this.carId, source.carId)
                && Objects.equals(this.areaOriginId, source.areaOriginId)
                && Objects.equals(this.areaSalesId, source.areaSalesId);
    }

    @Override
    public boolean isSelf(DeliveryCost source) {
        return Objects.equals(this.id, source.id);
    }
}