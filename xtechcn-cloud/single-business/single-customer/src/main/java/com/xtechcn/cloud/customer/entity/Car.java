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
 * 车辆管理
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@Getter
@Setter
@Schema(description = "车辆管理")
@TableName(value = "cu_car")
public class Car extends Model<Car> implements UniqueEntity<Car> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 车型
     */
    @Schema(description = "车型")
    private String name;
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
     * 创建时间
     */
    @Schema(description = "创建时间", hidden = true)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @Schema(description = "修改时间", hidden = true)
    private LocalDateTime updateTime;

    @Override
    public boolean isEquals(Car source) {
        return Objects.equals(this.type, source.type)
                && Objects.equals(this.name, source.name)
                && Objects.equals(this.length, source.length)
                && Objects.equals(this.width, source.width)
                && Objects.equals(this.height, source.height)
                && Objects.equals(this.weight, source.weight);
    }

    @Override
    public boolean isSelf(Car source) {
        return Objects.equals(this.id, source.id);
    }

    public Car withId() {
        Car car = new Car();
        car.id = this.id;
        return car;
    }

    public Car enable(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Car name(String name) {
        this.name = name;
        return this;
    }
}