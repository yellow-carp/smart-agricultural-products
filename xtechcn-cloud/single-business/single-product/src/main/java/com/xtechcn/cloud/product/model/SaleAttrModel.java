package com.xtechcn.cloud.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 销售属性
 *
 * @author Weijixiao
 * @since 2025-09-17 11:42
 */
@Getter
@Setter
public class SaleAttrModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 属性Id
     */
    @Schema(description = "属性Id")
    private Long attrId;
    /**
     * 销售属性名称
     */
    @Length(max = 31, message = "商品规格不能超过31个字符")
    @Schema(description = "销售属性名称")
    private String attrName;
    /**
     * 属性值
     */
    @Getter
    private List<AttrValue> attrValues;

    @Getter
    @Setter
    public static class AttrValue implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        /**
         * 属性值ID
         */
        @Schema(description = "属性值ID")
        private Long valueId;
        /**
         * 销售属性值
         */
        @Length(max = 31, message = "规格值不能超过31个字符")
        @Schema(description = "销售属性值")
        private String attrValue;
        /**
         * 展示图
         */
        @Schema(description = "展示图")
        private String image;
        /**
         * 选中状态
         */
        @Schema(description = "选中状态")
        private boolean checked;
        /**
         * 禁用状态
         */
        @Schema(description = "禁用状态")
        private boolean disabled;
        /**
         * 存在的SKU
         */
        @Schema(description = "存在的SKU")
        private String sku;
        /**
         * 库存
         */
        @Schema(description = "库存")
        private Integer inventory;

        public AttrValue image(String image) {
            this.image = image;
            return this;
        }

        public AttrValue checked(boolean checked) {
            this.checked = checked;
            return this;
        }

        public AttrValue disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }


        public AttrValue sku(String sku) {
            this.sku = sku;
            return this;
        }

        public AttrValue inventory(Integer inventory) {
            this.inventory = inventory;
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj instanceof AttrValue other) {
                // 忽略其他因素，直接比对属性值，因为能放在同一组集合中的销售属性值，是统一 SPU 的
                return Objects.equals(this.attrValue, other.getAttrValue());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.attrValue.hashCode();
        }
    }


    public static SaleAttrModel create(Long attrId, String attrName) {
        SaleAttrModel viewModel = new SaleAttrModel();
        viewModel.attrId = attrId;
        viewModel.attrName = attrName;
        viewModel.attrValues = new ArrayList<>();
        return viewModel;
    }


    public AttrValue addValue(Long valueId, String value) {
        AttrValue attrValue = new AttrValue();
        attrValue.valueId = valueId;
        attrValue.attrValue = value;
        this.attrValues.add(attrValue);
        return attrValue;
    }

    public SaleAttrModel chainValue(Long valueId, String value) {
        AttrValue attrValue = new AttrValue();
        attrValue.valueId = valueId;
        attrValue.attrValue = value;
        this.attrValues.add(attrValue);
        return this;
    }
}
