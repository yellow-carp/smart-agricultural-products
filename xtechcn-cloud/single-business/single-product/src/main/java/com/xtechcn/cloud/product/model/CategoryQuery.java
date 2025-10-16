package com.xtechcn.cloud.product.model;

import com.xtechcn.common.mybatis.wrapper.SqlCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * 分类查询参数
 *
 * @author Alay
 * @since 2025-06-26 14:08
 */
@Getter
@Setter
public class CategoryQuery {
    /**
     * 父节点id
     */
    private String pid;
    /**
     * 分类名称
     */
    @SqlCondition(type = SqlCondition.Type.LIKE)
    private String name;
}
