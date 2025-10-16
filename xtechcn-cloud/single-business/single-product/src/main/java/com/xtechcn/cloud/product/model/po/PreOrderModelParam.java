
package com.xtechcn.cloud.product.model.po;

import com.xtechcn.common.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 生成订单参数PO
 *
 * @author Dong
 * @date 2023-04-19 15:10
 */
@Getter
@Setter
@Schema(description = "生成订单参数")
public class PreOrderModelParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 操作者
     */
    @Schema(description = "操作者")
    private String userId;
    /**
     * 收货地址id
     */
    @Schema(description = "收货地址id")
    private String addressId;
    /**
     * 订单备注
     */
    @Schema(description = "订单备注")
    private String remark;
    /**
     * 来自购物车(1：来自购物车，2：直接下单)
     */
    @NotNull(message = "订单来源必填")
    @Schema(description = "订单来源:1-购物车，2-直接下单)")
    private Integer fromCart;
    /**
     * 订单信息
     */
    @Schema(description = "订单信息集合")
    @NotEmpty(message = "订单信息不能空")
    private List<SkuPoColl> orderInfoVo;

    @Getter
    @Setter
    public static class SkuPoColl implements Serializable{

        @Schema(description = "订单商品信息")
        private List<OrderSkuParam> orderDetailVos;
        /**
         * 排序
         */
        @Schema(description = "排序")
        private Integer sort;

        /**
         * 供应商id
         */
        @Schema(description = "供应商id")
        private String supplierId;
    }


    public void setUserId(Integer userId) {
        this.userId = SecurityUtil.userId();
    }
}
