package com.xtechcn.cloud.customer.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.mybatis.typehandler.Str2SetTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 采购商管理返回参数
 */

@Getter
@Setter
public class PurchaserView implements Serializable {
    /**
     * 主键id
     */
    @Schema(description = "主键id")
    private String id;
    /**
     * 采购商id
     */
    @Schema(description = "采购商id", hidden = true)
    private String userId;
    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;
    /**
     * 统一社会信用代码
     */
    @Schema(description = "统一社会信用代码")
    private String usci;
    /**
     * 采购商名称
     */
    @Schema(description = "采购商名称")
    private String name;
    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    private String contactName;
    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话")
    private String contactPhone;
    /**
     * 是否禁用(0:禁用,1：启用)
     */
    @Schema(description = "是否禁用(0:禁用,1：启用)")
    private Boolean enabled;
    /**
     * 钱包金额
     */
    @Schema(description = "钱包金额")
    private MoneyPenny registerAmount;
}
