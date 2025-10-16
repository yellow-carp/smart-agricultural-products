package com.xtechcn.cloud.customer.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class PackagingView implements Serializable {
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
}
