package com.xtechcn.cloud.product.listener;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品上架事件
 *
 * @author Weijixiao
 * @since 2025-09-17 15:54
 */
public record ProductTakeUpEvent(String spu) implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    public static ProductTakeUpEvent of(String spu) {
        return new ProductTakeUpEvent(spu);
    }

}