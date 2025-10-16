package com.xtechcn.cloud.product.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

/**
 * 规格名入参
 *
 * @author Alay
 * @since 2025-04-30 14:13
 */
@Setter
public class SpecNameParam {
    /**
     * 三级分类ID
     */
    @NotBlank(message = "三级分类Id不能空")
    @Schema(description = "三级分类ID")
    private String cat3Id;
    /**
     * 规格名ID
     */
    @Schema(description = "规格名ID")
    private Long nameId;
    /**
     * 规格名称
     */
    @NotBlank(message = "规格名不能空")
    @Schema(description = "规格名称")
    private String specName;

    public boolean isAdd() {
        return null == this.nameId;
    }

    public String cat3Id() {
        return this.cat3Id;
    }

    public Long nameId() {
        return this.nameId;
    }

    public String specName() {
        return this.specName;
    }
}
