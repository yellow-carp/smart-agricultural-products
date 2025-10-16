package com.xtechcn.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.product.entity.Category;
import com.xtechcn.cloud.product.model.CategoryTree;
import com.xtechcn.cloud.product.model.po.CategoryParam;
import com.xtechcn.common.core.lang.tree.ITreeChanger;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* 商品分类
*
* @author hanjie
* @since 2025-09-16 09:45:56
*/
public interface CategoryService extends IService<Category> {

    /**
     * 获取激活的分类树
     */
    List<CategoryTree> activeCategoryTree();

    /**
     * 查询所有激活的分类
     */
    List<Category> listActiveWithCache();

    /**
     * 查询所有分类
     */
    List<Category> listAllWithCache();

    /**
     * 获取所有分类树
     */
    List<CategoryTree> allCategoryTree();

    /**
     * 返回处理
     */
    List<CategoryTree> returnViewHandler(List<Category> categories);

    /**
     * 转换实体
     */
    Category convertEntity(CategoryParam categoryParam);

    /**
     * 新增初始化
     */
    void insertInit(Category category, Category parentCategory);

    /**
     * 数据唯一性验证
     */
    void uniqueCheck(Category category);

    /**
     * 父级处理
     */
    void insertAfterHandler(Category category, Category parentCategory);

    /**
     * 添加缓存
     */
    void addCache(Category category);

    /**
     * 查询孩子节点
     */
    List<Category> listChildren(Serializable pid);

    /**
     * 缓存中查询
     */
    Category findWithCache(String catId);
    /**
     * 缓存中查询
     */
    List<Category> listWithCache(Collection<String> catIds);
    /**
     * 修改初始化
     */
    void updateInit(Category category);

    /**
     * 刷新缓存
     */
    void refreshCache();

    /**
     * 修改变更处理
     */
    void processContext(ITreeChanger<Category> treeChanger);

    /**
     * 查找匪类家庭成员
     */
    List<Category> findFamily(Category category);

    /**
     * 删除菜单之后处理
     */
    void deleteAfterHandler(Category parentCategory);
}