package com.xtechcn.cloud.product.model.vo;

import com.xtechcn.cloud.product.entity.Category;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Weijixiao
 * @since 2025-09-17 11:38
 */
@Getter
public class CategoryView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 分类全路径
     */
    private String[] fullPath = new String[3];
    /**
     * 分类全名
     */
    private String[] fullName = new String[3];
    /**
     * 三级分类Id
     */
    private String cat3Id;

    public static CategoryView withCategories(List<Category> categories) {
        CategoryView categoryView = new CategoryView();
        categoryView.cat3Id = categories.get(2).getId();
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            categoryView.fullPath[i] = category.getId();
            categoryView.fullName[i] = category.getName();
        }
        return categoryView;
    }

}
