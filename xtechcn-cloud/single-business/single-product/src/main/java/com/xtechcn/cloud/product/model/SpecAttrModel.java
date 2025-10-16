package com.xtechcn.cloud.product.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品规格
 * [{"specName ":"颜色","specValue":"白色"},{"specName ":"材质","specValue":"棉"}]
 *
 * @author 唐杰
 * @description 商品规格:[{"specName ":"颜色","specValue":"白色"},{"specName ":"材质","specValue":"棉"}]
 * @since 2024-12-31 15:29
 */
@Getter
@Setter
public class SpecAttrModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 三级分类ID
     */
    private String cat3Id;
    /**
     * 主键Id自增
     */
    private Long nameId;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 属性值ID
     */
    private Long valueId;
    /**
     * 规格值
     */
    private String specValue;

    public static SpecAttrModel of(String cat3Id) {
        SpecAttrModel specAttrModel = new SpecAttrModel();
        specAttrModel.cat3Id = cat3Id;
        return specAttrModel;
    }

    public SpecAttrModel specName(Long nameId, String specName) {
        this.nameId = nameId;
        this.specName = specName;
        return this;
    }

    public SpecAttrModel specValue(Long valueId, String specValue) {
        this.valueId = valueId;
        this.specValue = specValue;
        return this;
    }

}
