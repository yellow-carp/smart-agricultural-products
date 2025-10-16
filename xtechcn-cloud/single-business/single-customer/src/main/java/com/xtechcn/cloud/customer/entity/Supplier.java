package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.UniqueEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 供应商管理
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@Getter
@Setter
@Schema(description = "供应商管理")
@TableName(value = "cu_supplier")
public class Supplier extends Model<Supplier> implements UniqueEntity<Supplier> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id", hidden = true)
    private String id;
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
     * 营业执照
     */
    @Schema(description = "营业执照")
    private String license;
    /**
     * 是否禁用(0:禁用,1：启用)
     */
    @Schema(description = "是否禁用(0:禁用,1：启用)")
    private Boolean enabled;
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
     * 店铺图片
     */
    @Schema(description = "店铺图片")
    private String shopImage;
    /**
     * 注册地址
     */
    @Schema(description = "注册地址")
    private String address;
    /**
     * 管理类型(0:经办人，1：法人)
     */
    @Schema(description = "管理类型(0:经办人，1：法人)")
    private Integer manageType;
    /**
     * 申请状态(1：编辑中、2：待审核、3：已通过、4：已拒绝)
     */
    @Schema(description = "申请状态(1：编辑中、2：待审核、3：已通过、4：已拒绝)")
    private Integer state;
    /**
     * openId
     */
    @Schema(description = "openId")
    private String openId;
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
     * 审核信息
     */
    @Schema(description = "审核信息")
    private String checkMsg;
    /**
     * 申请人
     */
    @Schema(description = "申请人", hidden = true)
    private String userId;
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

    public Supplier withId() {
        Supplier supplier = new Supplier();
        supplier.id = this.id;
        return supplier;
    }

    public Supplier usci(String usci) {
        this.usci = usci;
        return this;
    }

    public Supplier name(String name) {
        this.name = name;
        return this;
    }

    public Supplier juridicalUser(String juridicalUser) {
        this.juridicalUser = juridicalUser;
        return this;
    }

    public Supplier areaId(String areaId) {
        this.areaId = areaId;
        return this;
    }

    public Supplier contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public Supplier contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public Supplier contactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }

    public Supplier license(String license) {
        this.license = license;
        return this;
    }

    public Supplier enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Supplier certStartTime(LocalDate certStartTime) {
        this.certStartTime = certStartTime;
        return this;
    }

    public Supplier certEndTime(LocalDate certEndTime) {
        this.certEndTime = certEndTime;
        return this;
    }

    public Supplier shopImage(String shopImage) {
        this.shopImage = shopImage;
        return this;
    }

    public Supplier address(String address) {
        this.address = address;
        return this;
    }

    public Supplier manageType(Integer manageType) {
        this.manageType = manageType;
        return this;
    }

    public Supplier state(Integer state) {
        this.state = state;
        return this;
    }

    public Supplier openId(String openId) {
        this.openId = openId;
        return this;
    }

    public Supplier attitude(Double attitude) {
        this.attitude = attitude;
        return this;
    }

    public Supplier satisfied(Double satisfied) {
        this.satisfied = satisfied;
        return this;
    }

    public Supplier score(Double score) {
        this.score = score;
        return this;
    }

    public Supplier checkMsg(String checkMsg) {
        this.checkMsg = checkMsg;
        return this;
    }

    public Supplier userId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public boolean isEquals(Supplier source) {
        return Objects.equals(this.usci, source.usci)
                && Objects.equals(this.name, source.name)
                && Objects.equals(this.openId, source.openId);
    }

    @Override
    public boolean isSelf(Supplier source) {
        return Objects.equals(this.id, source.id);
    }
}