package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.utils.IParamUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.time.LocalDateTime;

/**
* 商品SKU销售属性值
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "商品SKU销售属性值")
@TableName(value = "pd_sku_sale_attr")
public class SkuSaleAttr extends Model<SkuSaleAttr>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键id自增
    */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键id自增", hidden=true)
    private Long id;
    /**
    * 商品spu
    */
    @Schema(description = "商品spu")
    private String spu;
    /**
    * 商品sku
    */
    @Schema(description = "商品sku")
    private String sku;
    /**
    * 属性Id
    */
    @Schema(description = "属性Id")
    private Long attrId;
    /**
    * 属性值Id
    */
    @Schema(description = "属性值Id")
    private Long valueId;

    public static SkuSaleAttr create(String spu, String sku, Long attrId, Long valueId) {
        SkuSaleAttr skuSaleAttr = new SkuSaleAttr();
        skuSaleAttr.spu = spu;
        skuSaleAttr.sku = sku;
        skuSaleAttr.attrId = attrId;
        skuSaleAttr.valueId = valueId;
        return skuSaleAttr;
    }

    public SkuSaleAttr changeValue(SkuSaleAttr source) {
        this.spu = source.spu;
        this.sku = source.sku;
        this.valueId = source.valueId;
        return this;
    }

    public SkuSaleAttr valueId(Long valueId) {
        this.valueId = valueId;
        return this;
    }


    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (obj == this) return true;
        if (obj instanceof SkuSaleAttr source) {
            return IParamUtil.nonNullEquals(this.attrId, source.attrId)
                    && IParamUtil.nonNullEquals(this.sku, source.sku)
                    && IParamUtil.nonNullEquals(this.valueId, source.valueId);
        }
        return false;
    }
}