package com.xtechcn.cloud.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购商入驻配置
 *
 * @author hanjie
 * @since 2025-09-16 09:36:46
 */
@Getter
@Setter
@Schema(description = "采购商入驻配置")
@TableName(value = "cu_purchaser_config")
public class PurchaserConfig extends Model<PurchaserConfig> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 入驻费用
     */
    @Schema(description = "入驻费用")
    private MoneyPenny registerAmount;
    /**
     * 邀请码
     */
    @Schema(description = "邀请码")
    private String inviteCode;
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

}