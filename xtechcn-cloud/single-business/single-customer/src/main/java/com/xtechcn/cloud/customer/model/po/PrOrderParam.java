package com.xtechcn.cloud.customer.model.po;

import com.xtechcn.common.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Hanjie
 * @since 2025-09-18 17:18
 */
@Getter
public class PrOrderParam implements Serializable {
    /**
     * 操作者
     */
    @Schema(description = "操作者")
    private String userId;

    /**
     * 订单备注
     */
    @Setter
    @Schema(description = "订单备注")
    private String remark;

    public void setUserId(Integer userId) {
        this.userId = SecurityUtil.userId();
    }
}
