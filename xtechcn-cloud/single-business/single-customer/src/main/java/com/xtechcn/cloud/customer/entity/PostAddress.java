package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.cloud.customer.model.vo.PostAddressView;
import com.xtechcn.common.core.lang.UniqueEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 邮递地址信息表
 *
 * @author hanjie
 * @since 2025-09-11 17:59:28
 */
@Getter
@Setter
@Schema(description = "邮递地址信息表")
@TableName(value = "cu_post_address")
public class PostAddress extends Model<PostAddress> implements UniqueEntity<PostAddress> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 采购商id
     */
    @Schema(description = "采购商id", hidden = true)
    private String userId;
    /**
     * 行政区域编码
     */
    @Schema(description = "行政区域编码")
    private String regionCode;
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
     * 邮政编码
     */
    @Schema(description = "邮政编码")
    private String zip;
    /**
     * 详细地址
     */
    @Schema(description = "详细地址")
    private String detail;
    /**
     * 公司名称
     */
    @Schema(description = "公司名称")
    private String comName;
    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名")
    private String contact;
    /**
     * 收货人电话
     */
    @Schema(description = "收货人电话")
    private String contactTel;
    /**
     * 是否为默认地址
     */
    @Schema(description = "是否为默认地址")
    private Boolean isDefault;
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
    public boolean isEquals(PostAddress source) {
        return Objects.equals(this.regionCode, source.regionCode);
    }

    @Override
    public boolean isSelf(PostAddress source) {
        return Objects.equals(this.id, source.id);
    }
}