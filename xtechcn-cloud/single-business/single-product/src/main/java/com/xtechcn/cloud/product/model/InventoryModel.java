package com.xtechcn.cloud.product.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 库存数据体
 * @author Weijixiao
 * @since 2025-09-17 15:30
 */
@Getter
@Setter
public class InventoryModel {
    /**
     * SKU 编码
     */
    private String spu;
    /**
     * SKU 编码
     */
    private String sku;
    /**
     * SKU 编码
     */
    private String title;
    /**
     * 库存数
     */
    private Integer quantity;

    public static InventoryModel of(String spu, String sku) {
        InventoryModel model = new InventoryModel();
        model.spu = spu;
        model.sku = sku;
        return model;
    }

    public InventoryModel title(String title) {
        this.title = title;
        return this;
    }

    public InventoryModel quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}
