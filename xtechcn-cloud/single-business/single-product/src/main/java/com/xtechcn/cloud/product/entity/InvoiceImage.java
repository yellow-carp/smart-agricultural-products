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
* 发票信息
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "发票信息")
@TableName(value = "pd_invoice_image")
public class InvoiceImage extends Model<InvoiceImage>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键id
    */
    @TableId
    @Schema(description = "主键id", hidden=true)
    private String id;
    /**
    * 订单编号
    */
    @Schema(description = "订单编号")
    private String orderNo;
    /**
    * 发票图片
    */
    @Schema(description = "发票图片")
    private String image;
    /**
    * 创建时间
    */
    @Schema(description = "创建时间", hidden=true)
    private LocalDateTime createTime;
    /**
    * 更新时间
    */
    @Schema(description = "更新时间", hidden=true)
    private LocalDateTime updateTime;

}