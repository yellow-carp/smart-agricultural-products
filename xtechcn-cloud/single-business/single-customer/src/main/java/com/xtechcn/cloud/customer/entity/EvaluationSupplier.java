package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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

/**
 * 评价管理
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@Getter
@Setter
@Schema(description = "评价管理")
@TableName(value = "cu_evaluation_supplier")
public class EvaluationSupplier extends Model<EvaluationSupplier> {
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
    @Schema(description = "产区id", hidden = true)
    private String userId;
    /**
     * 供应商Id
     */
    @Schema(description = "供应商Id")
    private String supplierId;
    /**
     * 订单编号
     */
    @Schema(description = "订单编号")
    private String orderNo;
    /**
     * 服务态度
     */
    @Schema(description = "服务态度")
    private Double attitude;
    /**
     * 满意度
     */
    @Schema(description = "满意度")
    private Double satisfied;
    /**
     * 综合评分
     */
    @Schema(description = "综合评分")
    private Double score;
    /**
     * 评价内容
     */
    @Schema(description = "评价内容")
    private String content;
    /**
     * 是否隐藏
     */
    @Schema(description = "是否隐藏")
    private Boolean isHide;
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
     * 评价时间
     */
    @Schema(description = "评价时间", hidden = true)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @Schema(description = "修改时间", hidden = true)
    private LocalDateTime updateTime;

}