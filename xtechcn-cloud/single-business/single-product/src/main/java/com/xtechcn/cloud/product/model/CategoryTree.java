package com.xtechcn.cloud.product.model;

import com.xtechcn.common.core.lang.tree.ITreeNode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类树形
 *
 * @author Alay
 * @since 2024-03-09 10:50
 */
@Getter
@Setter
public class CategoryTree implements ITreeNode<CategoryTree> {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private String id;
    /**
     * 分类层级
     */
    private Integer level;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 上级 id
     */
    private String pid;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 更多信息描述
     */
    private String notes;
    /**
     * 全路径
     */
    private String fullPath;
    /**
     * 是否有子节点
     */
    private Boolean hasChildren;
    /**
     * 孩列节点
     */
    protected List<CategoryTree> children;

    @Override
    public String code() {
        return id;
    }

    @Override
    public String parentCode() {
        return pid;
    }

    @Override
    public Integer level() {
        return level;
    }

    @Override
    public void addChildren(CategoryTree child) {
        if (null == this.children) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    public void hasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
