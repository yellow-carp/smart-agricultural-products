package com.xtechcn.cloud.customer.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.xtechcn.cloud.customer.model.dto.RegionDto;
import com.xtechcn.common.serialization.lang.JsonObj;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import netscape.javascript.JSObject;

import java.util.List;

@Setter
@Getter
public class AreaView {
    /**
     * 主键id
     */
    @Schema(description = "主键id", hidden = true)
    private String id;
    /**
     * 区域名称
     */
    @Schema(description = "区域名称")
    private String name;
    /**
     * 区域类型：1-产区，2-销区
     */
    @Schema(description = "区域类型：1-产区，2-销区")
    private Integer type;
    /**
     * 物流模式：1-整车，2-整车+拼车
     */
    @Schema(description = "物流模式：1-整车，2-整车+拼车")
    private Integer deliveryModel;
    /**
     * 启用禁用
     */
    @Schema(description = "启用禁用")
    private Boolean enabled;
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
    @Schema(description = "行政区域编码列表")
    private List<RegionDto> regionCodeList;
}
