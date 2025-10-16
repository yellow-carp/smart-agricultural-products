package com.xtechcn.cloud.customer.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xtechcn.cloud.customer.model.dto.RegionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import netscape.javascript.JSObject;

import java.util.List;

@Getter
@Setter
public class AreaParam {
    /**
     * 主键id
     */
    @Schema(description = "主键id")
    private String id;
    /**
     * 区域名称
     */
    @NotBlank(message = "区域名称不能为空")
    @Schema(description = "区域名称")
    private String name;
    /**
     * 区域类型：1-产区，2-销区
     */
    @NotNull(message = "区域类型不能为空")
    @Schema(description = "区域类型：1-产区，2-销区")
    private Integer type;
    /**
     * 物流模式：1-整车，2-整车+拼车
     */
    @NotNull(message = "物流模式不能为空")
    @Schema(description = "物流模式：1-整车，2-整车+拼车")
    private Integer deliveryModel;
    /**
     * 行政区域全路径
     */
    @Schema(description = "行政区域全路径")
    private String regionFullPath;
    /**
     * 行政区域全名
     */
    @Schema(description = "行政区域全名")
    private String regionFullName;
    /**
     * 行政区域编码列表
     */
    @NotEmpty(message = "regionCodeList不能为空且至少包含一个元素")
    @Schema(description = "行政区域编码列表（每个编码为6位数字）")
    private List<RegionDto> regionCodeList;

    public boolean isAdd() {
        return null == this.id;
    }
}
