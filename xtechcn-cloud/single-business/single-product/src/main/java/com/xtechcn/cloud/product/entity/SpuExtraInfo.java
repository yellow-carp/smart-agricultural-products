package com.xtechcn.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xtechcn.common.mybatis.typehandler.Str2SetTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;
import java.util.Set;

/**
* 商品额外的信息
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@Getter
@Setter
@Schema(description = "商品额外的信息")
@TableName(value = "pd_spu_extra_info")
public class SpuExtraInfo extends Model<SpuExtraInfo>{
    @Serial
    private static final long serialVersionUID=1L;
    /**
    * 主键spu
    */
    @TableId
    @Schema(description = "主键spu")
    private String spu;
    /**
    * 详情富文本
    */
    @Schema(description = "详情富文本")
    private String details;
    /**
    * 商品多图
    */
    @Schema(description = "商品多图")
    private List<String> images;
    /**
    * 审核信息
    */
    @Schema(description = "审核信息")
    private String checkMsg;
    /**
    * 售后政策
    */
    @Schema(description = "售后政策")
    private Integer afterPolicy;
    /**
    * 最小购买
    */
    @Schema(description = "最小购买")
    private Integer limitMin;
    /**
    * 最小购买
    */
    @Schema(description = "最小购买")
    private Integer limitOnce;
    /**
     * 是否仅展示：0-否，1-是。
     */
    @Schema(description = "是否仅展示：0-否，1-是。")
    private Boolean isShowOnly;
    /**
     * 是否公开 0-不公开 1-公开
     */
    @Schema(description = "是否公开 0-不公开 1-公开")
    private Boolean isPublic;
    /**
     * 二级单位 id，多个以逗号隔开
     */
    @TableField(typeHandler = Str2SetTypeHandler.class)
    @Schema(description = "二级单位 id，多个以逗号隔开")
    private Set<String> deptIds;

    public static SpuExtraInfo ofSpu(String spu) {
        SpuExtraInfo spuExtraInfo = new SpuExtraInfo();
        spuExtraInfo.spu = spu;
        return spuExtraInfo;
    }
}