package com.xtechcn.cloud.customer.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xtechcn.common.mybatis.wrapper.AutoUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class PackagingParam {
    /**
     * 主键id自增
     */
    @Schema(description = "主键id自增", hidden = true)
    private Long id;
    /**
     * 单位名称
     */
    @Schema(description = "单位名称")
    @NotNull(message = "单位名称不能为空")
    private String name;
    /**
     * 长（cm）
     */
    @Schema(description = "长（cm）")
    @Min(value = 0, message = "长不能小于0")
    private Double length;
    /**
     * 宽（cm）
     */
    @Schema(description = "宽（cm）")
    @Min(value = 0, message = "宽不能小于0")
    private Double width;
    /**
     * 高（cm）
     */
    @Schema(description = "高（cm）")
    @Min(value = 0, message = "高不能小于0")
    private Double height;

    public boolean isAdd() {
        return null == this.id;
    }
}
