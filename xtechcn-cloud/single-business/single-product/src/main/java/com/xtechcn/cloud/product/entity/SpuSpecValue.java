package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* spu规格属性值
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "spu规格属性值")
@TableName(value = "pd_spu_spec_value")
public class SpuSpecValue extends Model<SpuSpecValue>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键id自增
    */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键id自增", hidden=true)
    private Long id;
    /**
    * 商品Spu
    */
    @Schema(description = "商品Spu")
    private String spu;
    /**
    * 规格名Id
    */
    @Schema(description = "规格名Id")
    private Long nameId;
    /**
    * 规格属性值
    */
    @Schema(description = "规格属性值")
    private String specValue;
    /**
    * 是否加入搜索
    */
    @Schema(description = "是否加入搜索")
    private Boolean inSearch;


    public static SpuSpecValue ofSpu(String spu) {
        SpuSpecValue spuSpecValue = new SpuSpecValue();
        spuSpecValue.spu = spu;
        return spuSpecValue;
    }

    public SpuSpecValue withId(Long valueId) {
        this.id = valueId;
        return this;
    }

    public SpuSpecValue andName(Long nameId, String specValue) {
        this.nameId = nameId;
        this.specValue = specValue;
        return this;
    }

    public SpuSpecValue inSearch(boolean inSearch) {
        this.inSearch = inSearch;
        return this;
    }
}