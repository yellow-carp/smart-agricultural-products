package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.core.lang.MoneyPenny;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.io.Serial;
    import java.math.BigDecimal;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.baomidou.mybatisplus.annotation.TableLogic;
import java.time.LocalDateTime;

/**
* 发货单表
*
* @author hanjie
* @since 2025-09-16 09:45:54
*/
@Getter
@Setter
@Schema(description = "发货单表")
@TableName(value = "pd_shipment")
public class Shipment extends Model<Shipment>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键id
    */
    @TableId
    @Schema(description = "主键id", hidden=true)
    private String id;
    /**
    * 物流模式：1-整车，2-拼车
    */
    @Schema(description = "物流模式：1-整车，2-拼车")
    private Integer deliveryModel;
    /**
    * 产区id
    */
    @Schema(description = "产区id")
    private String areaOriginId;
    /**
    * 销区id
    */
    @Schema(description = "销区id")
    private String areaSalesId;
    /**
    * 车牌号
    */
    @Schema(description = "车牌号")
    private String carNumber;
    /**
    * 司机
    */
    @Schema(description = "司机")
    private String driver;
    /**
    * 司机联系方式
    */
    @Schema(description = "司机联系方式")
    private String driverPhone;
    /**
    * 车型id
    */
    @Schema(description = "车型id")
    private String column12;
    /**
    * 实际运费
    */
    @Schema(description = "实际运费")
    private MoneyPenny actualFreight;
    /**
    * 备注
    */
    @Schema(description = "备注")
    private String remark;
    /**
    * 物流公司名称
    */
    @Schema(description = "物流公司名称")
    private String deliveryName;
    /**
    * 物流公司ID
    */
    @Schema(description = "物流公司ID")
    private String deliveryId;
    /**
    * 物流单号
    */
    @Schema(description = "物流单号")
    private String trackingNo;
    /**
    * 逻辑删除
    */
    @JsonIgnore
    @TableLogic
    @TableField(select = false)
    @Schema(description = "逻辑删除", hidden=true)
    private Boolean isDelete;
    /**
    * 创建时间
    */
    @Schema(description = "创建时间", hidden=true)
    private LocalDateTime createTime;
    /**
    * 修改时间
    */
    @Schema(description = "修改时间", hidden=true)
    private LocalDateTime updateTime;

}