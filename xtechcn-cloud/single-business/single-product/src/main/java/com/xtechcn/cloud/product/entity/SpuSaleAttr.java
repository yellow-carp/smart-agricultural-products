package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* SPU销售属性
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "SPU销售属性")
@TableName(value = "pd_spu_sale_attr")
public class SpuSaleAttr extends Model<SpuSaleAttr>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键id自增
    */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键id自增", hidden=true)
    private Long id;
    /**
    * 商品SPU
    */
    @Schema(description = "商品SPU")
    private String spu;
    /**
    * 属性名
    */
    @Schema(description = "属性名")
    private String name;
    /**
    * 排序
    */
    @Schema(description = "排序")
    private Integer sort;
    /**
    * 操作者
    */
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "操作者", hidden=true)
    private String upsertBy;
    /**
    * 操作时间
    */
    @Schema(description = "操作时间", hidden=true)
    private LocalDateTime upsertTime;


    public static SpuSaleAttr create(String spu, String name, Integer sort) {
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpu(spu);
        spuSaleAttr.setName(name);
        spuSaleAttr.setSort(sort);
        return spuSaleAttr;
    }

    public SpuSaleAttr id(Long id) {
        this.id = id;
        return this;
    }
}