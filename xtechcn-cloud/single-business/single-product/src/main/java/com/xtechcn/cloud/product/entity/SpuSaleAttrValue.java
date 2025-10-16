package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.time.LocalDateTime;

/**
* 商品销售属性值表
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "商品销售属性值表")
@TableName(value = "pd_spu_sale_attr_value")
public class SpuSaleAttrValue extends Model<SpuSaleAttrValue>{
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
    * 属性Id
    */
    @Schema(description = "属性Id")
    private Long attrId;
    /**
    * 属性值
    */
    @Schema(description = "属性值")
    private String attrValue;
    /**
    * 展示图
    */
    @Schema(description = "展示图")
    private String image;
    /**
    * 排序
    */
    @Schema(description = "排序")
    private Integer sort;

    public static SpuSaleAttrValue create(String spu, Long attrId, String attrValue) {
        SpuSaleAttrValue saleAttrValue = new SpuSaleAttrValue();
        saleAttrValue.spu = spu;
        saleAttrValue.attrId = attrId;
        saleAttrValue.attrValue = attrValue;
        return saleAttrValue;
    }


    public SpuSaleAttrValue image(String image) {
        this.image = image;
        return this;
    }

    public SpuSaleAttrValue sort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public SpuSaleAttrValue withId(Long valueId) {
        this.id = valueId;
        return this;
    }
}