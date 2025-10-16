package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.UniqueEntity;
import com.xtechcn.common.mybatis.typehandler.Str2SetTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 采购商管理
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@Getter
@Setter
@Schema(description = "采购商管理")
@TableName(value = "cu_purchaser")
public class Purchaser extends Model<Purchaser> implements UniqueEntity<Purchaser> {
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
    @Schema(description = "采购商id")
    private String userId;
    /**
     * 统一社会信用代码
     */
    @Schema(description = "统一社会信用代码")
    private String usci;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称")
    private String name;
    /**
     * 申请状态
     */
    @Schema(description = "申请状态： 1-未入驻、2-已入驻")
    private Integer state;
    /**
     * 法人
     */
    @Schema(description = "法人")
    private String juridicalUser;
    /**
     * 所属产区id
     */
    @Schema(description = "所属产区id")
    private String areaId;
    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    private String contactName;
    /**
     * 联系人邮箱
     */
    @Schema(description = "联系人邮箱")
    private String contactEmail;
    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话")
    private String contactPhone;
    /**
     * 门头照片
     */
    @Schema(description = "门头照片")
    @TableField(typeHandler = Str2SetTypeHandler.class)
    private List<String> storefrontPhoto;
    /**
     * 营业执照
     */
    @Schema(description = "营业执照")
    private String license;
    /**
     * 营业执照生效日
     */
    @Schema(description = "营业执照生效日")
    private LocalDate certStartTime;
    /**
     * 营业执照失效日
     */
    @Schema(description = "营业执照失效日")
    private LocalDate certEndTime;
    /**
     * 是否禁用(0:禁用,1：启用)
     */
    @Schema(description = "是否禁用(0:禁用,1：启用)")
    private Boolean enabled;
    /**
     * 注册地址
     */
    @Schema(description = "注册地址")
    private String address;
    /**
     * 入驻方式：1-邀请码，2-支付入驻
     */
    @Schema(description = "入驻方式：1-邀请码，2-支付入驻,3-后台添加")
    private Integer registerModel;
    /**
     * 支付订单号
     */
    @Schema(description = "支付订单号")
    private String orderNo;
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


    public Purchaser withId() {
        Purchaser purchaser = new Purchaser();
        purchaser.id = this.id;
        return purchaser;
    }

    public Purchaser usci(String usci) {
        this.usci = usci;
        return this;
    }

    public Purchaser name(String name) {
        this.name = name;
        return this;
    }

    public Purchaser juridicalUser(String juridicalUser) {
        this.juridicalUser = juridicalUser;
        return this;
    }

    public Purchaser areaId(String areaId) {
        this.areaId = areaId;
        return this;
    }

    public Purchaser contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public Purchaser contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public Purchaser contactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }

    public Purchaser license(String license) {
        this.license = license;
        return this;
    }

    public Purchaser enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Purchaser certStartTime(LocalDate certStartTime) {
        this.certStartTime = certStartTime;
        return this;
    }

    public Purchaser certEndTime(LocalDate certEndTime) {
        this.certEndTime = certEndTime;
        return this;
    }

    public Purchaser address(String address) {
        this.address = address;
        return this;
    }

    public Purchaser userId(String userId) {
        this.userId = userId;
        return this;
    }

    public Purchaser storefrontPhoto(List<String> storefrontPhoto) {
        this.storefrontPhoto = storefrontPhoto;
        return this;
    }

    public Purchaser state(Integer state) {
        this.state = state;
        return this;
    }

    public Purchaser registerModel(Integer registerModel) {
        this.registerModel = registerModel;
        return this;
    }

    public Purchaser orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    @Override
    public boolean isEquals(Purchaser source) {
        return Objects.equals(this.userId, source.userId);
    }

    @Override
    public boolean isSelf(Purchaser source) {
        return Objects.equals(this.id, source.id);
    }
}