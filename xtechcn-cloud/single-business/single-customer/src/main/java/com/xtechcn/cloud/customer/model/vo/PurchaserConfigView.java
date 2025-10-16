package com.xtechcn.cloud.customer.model.vo;

import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class PurchaserConfigView {
    /**
     * 主键id
     */
    @Schema(description = "主键id", hidden=true)
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
}
