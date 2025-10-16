package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.UniqueEntity;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * spu规格名称
 *
 * @author hanjie
 * @since 2025-09-16 09:45:55
 */
@Getter
@Setter
@Schema(description = "spu规格名称")
@TableName(value = "pd_spec_name")
public class SpecName extends Model<SpecName> implements UniqueEntity<SpecName> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键Id自增
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键Id自增", hidden = true)
    private Long id;
    /**
     * 商品三级分类id
     */
    @Schema(description = "商品三级分类id")
    private String cat3Id;
    /**
     * 规格名
     */
    @Schema(description = "规格名")
    private String specName;
    /**
     * 操作者
     */
    @AutoUser
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "操作者", hidden = true)
    private String upsertBy;
    /**
     * 操作时间
     */
    @Schema(description = "操作时间", hidden = true)
    private LocalDateTime upsertTime;


    public static SpecName create(String cat3Id, Long id, String specName) {
        SpecName specEntity = new SpecName();
        specEntity.cat3Id = cat3Id;
        specEntity.id = id;
        specEntity.specName = specName;
        return specEntity;
    }

    @Override
    public boolean isEquals(SpecName source) {
        return Objects.equals(this.cat3Id, source.cat3Id) && Objects.equals(this.specName, source.specName);
    }

    @Override
    public boolean isSelf(SpecName source) {
        return Objects.equals(this.id, source.id);
    }
}