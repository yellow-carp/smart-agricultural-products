package com.xtechcn.cloud.product.model.po;

import com.xtechcn.common.core.constants.TreeConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类参数对象
 *
 * @author Alay
 * @since 2024-03-09 12:55
 */
@Getter
@Setter
public class CategoryParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键自增id
     */
    private String id;
    /**
     * 父节点id
     */
    private String pid = TreeConstant.TREE_ROOT_CODE;
    /**
     * 分类名称
     */
    @NotBlank(message = "名称不能空")
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 背景图
     */
    private String bgPic;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 说明描述
     */
    private String notes;
}
