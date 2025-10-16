package com.xtechcn.cloud.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.constants.ProductConstants;
import com.xtechcn.cloud.product.constants.ProductResult;
import com.xtechcn.cloud.product.entity.Category;
import com.xtechcn.cloud.product.mapper.CategoryMapper;
import com.xtechcn.cloud.product.model.CategoryTree;
import com.xtechcn.cloud.product.model.po.CategoryParam;
import com.xtechcn.cloud.product.service.CategoryService;
import com.xtechcn.common.core.constants.TreeConstant;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.lang.tree.ITreeChanger;
import com.xtechcn.common.core.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;


/**
 * 商品分类
 *
 * @author hanjie
 * @since 2025-09-16 09:45:56
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final RedisTemplate<String, ?> redisTemplate;

    @Override
    @Cacheable(value = CacheConstants.CATEGORY_TREE_CACHE, key = "'active'", unless = "#result.isEmpty()")
    public List<CategoryTree> activeCategoryTree() {
        List<Category> categories = this.listActiveWithCache();
        if (categories.isEmpty()) return new ArrayList<>();
        categories = categories.stream().sorted(Comparator.comparing(Category::getSort)).toList();
        return ITreeUtil.buildTree(categories, category -> {
            return BeanUtil.copyProperties(category, CategoryTree.class);
        });
    }

    @Override
    public List<Category> listActiveWithCache() {
        List<Category> categories = this.listAllWithCache();
        if (categories.isEmpty()) return new ArrayList<>();
        // 过滤激活的分类数据
        return categories.stream().filter(Category::getEnabled).toList();
    }

    @Override
    public List<Category> listAllWithCache() {
        String cacheKey = CacheConstants.CATEGORY_HASH_KEY + ":" + "all";
        Map<String, Category> entries = redisTemplate.<String, Category>opsForHash().entries(cacheKey);
        long count = this.count();
        if (entries.isEmpty() || entries.size() != count) {
            // 查询全部的数据
            List<Category> allCategories = this.list();
            if (!allCategories.isEmpty()) {
                allCategories.forEach(category -> redisTemplate.<String, Category>opsForHash().put(cacheKey, category.getId(), category));
            }
            return allCategories;
        }
        return entries.values().stream().toList();
    }

    @Override
    @Cacheable(value = CacheConstants.CATEGORY_TREE_CACHE, key = "'all'", unless = "#result.isEmpty()")
    public List<CategoryTree> allCategoryTree() {
        List<Category> categories = this.listAllWithCache();
        if (categories.isEmpty()) return new ArrayList<>();
        categories = categories.stream().sorted(Comparator.comparing(Category::getSort)).toList();
        return ITreeUtil.buildTree(categories, category -> {
            return BeanUtil.copyProperties(category, CategoryTree.class);
        });
    }

    @Override
    public List<CategoryTree> returnViewHandler(List<Category> categories) {
        List<CategoryTree> categoryTrees = new ArrayList<>();
        for (Category category : categories) {
            CategoryTree categoryTree = new CategoryTree();
            categoryTree.setId(category.getId());
            categoryTree.setLevel(category.getLevel());
            categoryTree.setSort(category.getSort());
            categoryTree.setEnabled(category.getEnabled());
            categoryTree.setPid(category.getPid());
            categoryTree.setName(category.getName());
            categoryTree.setNotes(category.getNotes());
            categoryTree.setFullPath(category.getFullPath());
            categoryTree.hasChildren(category.getHasChildren());
            categoryTrees.add(categoryTree);
        }
        return categoryTrees;
    }

    @Override
    public Category convertEntity(CategoryParam categoryParam) {
        Category category = new Category();
        category.setId(categoryParam.getId());
        category.setPid(categoryParam.getPid());
        category.setName(categoryParam.getName());
        category.setIcon(categoryParam.getIcon());
        category.setBgPic(categoryParam.getBgPic());
        category.setSort(categoryParam.getSort());
        category.setNotes(categoryParam.getNotes());
        return category;
    }

    @Override
    public void insertInit(Category category, Category parentCategory) {
        // 排序默认值处理
        IParamUtil.setProperty(category, Category::setSort, category.getSort(), 0);
        // 顶级分类
        if (null == parentCategory) {
            category.setLevel(TreeConstant.TREE_TOP_LEVEL);
        } else {
            category.setLevel(parentCategory.getLevel() + 1);
            if (ProductConstants.LOW_CAT_LEVEL < category.getLevel()) {
                throw new ParamException("商品最多只能三个分类级别");
            }
        }
        // 新增时默认为非启用状态
        category.setEnabled(false);
        // 前端传参空字符串处理为 null
        IParamUtil.setStrProperty(category, Category::setIcon, category.getIcon(), (String) null);
        IParamUtil.setStrProperty(category, Category::setBgPic, category.getBgPic(), (String) null);
        IParamUtil.setStrProperty(category, Category::setNotes, category.getNotes(), (String) null);
    }

    @Override
    public void uniqueCheck(Category category) {
        List<Category> categories = this.listChildren(category.getPid());
        if (categories.isEmpty()) return;
        for (Category categoryInDb : categories) {
            if (categoryInDb.equalsAndExcludeSelf(category)) throw new UniqueException();
        }
    }

    @Override
    public void insertAfterHandler(Category category, Category parentCategory) {
        // 顶级节点
        if (null == parentCategory) {
            category.setFullPath(category.getId());
        } else {
            // 非顶级节点
            category.setLevel(parentCategory.getLevel() + 1);
            category.setFullPath(parentCategory.getFullPath() + IStrPool.CARET + category.getId());
            parentCategory.plusChild();
            // 更新父节点
            this.updateById(parentCategory);
        }
        // 更新节点
        this.updateById(category);
    }

    @Override
    @CacheEvict(value = CacheConstants.CATEGORY_TREE_CACHE, allEntries = true)
    public void addCache(Category category) {
        String cacheKey = CacheConstants.CATEGORY_HASH_KEY + ":" + "all";
        redisTemplate.<String, Category>opsForHash().put(cacheKey, category.getId(), category);
    }

    @Override
    public List<Category> listChildren(Serializable pid) {
        return this.list(Wrappers.<Category>lambdaQuery().eq(Category::getPid, pid));
    }

    @Override
    public Category findWithCache(String catId) {
        String cacheKey = CacheConstants.CATEGORY_HASH_KEY + ":" + "all";
        Category category = (Category) redisTemplate.<String, Category>opsForHash().get(cacheKey, catId);
        if (null != category) return category;
        category = this.getById(catId);
        if (null != category) {
            redisTemplate.<String, Category>opsForHash().put(cacheKey, catId, category);
        }
        return category;
    }

    @Override
    public List<Category> listWithCache(Collection<String> catIds) {
        String cacheKey = CacheConstants.CATEGORY_HASH_KEY + ":" + "all";
        List<Category> categories = redisTemplate.<String, Category>opsForHash().multiGet(cacheKey, catIds);
        if (ICollUtil.isNotEmpty(categories, true) && categories.size() == catIds.size()) return categories;

        // 混存中没数据,更新缓存
        categories = this.listByIds(catIds);
        if (!categories.isEmpty()) {
            redisTemplate.<String, Category>opsForHash().putAll(cacheKey, IMapUtil.coll2Map(categories, Category::getId));
        }
        return categories;
    }

    @Override
    public void updateInit(Category category) {
        // 前端传参空字符串处理为 null
        IParamUtil.setStrProperty(category, Category::setIcon, category.getIcon(), (String) null);
        IParamUtil.setStrProperty(category, Category::setBgPic, category.getBgPic(), (String) null);
        IParamUtil.setStrProperty(category, Category::setNotes, category.getNotes(), (String) null);
    }

    @Override
    @CacheEvict(value = CacheConstants.CATEGORY_TREE_CACHE, allEntries = true)
    public void refreshCache() {
        String cacheKey = CacheConstants.CATEGORY_HASH_KEY.concat(":all");
        redisTemplate.delete(cacheKey);
        List<Category> allCategories = this.listAllWithCache();
        if (allCategories.isEmpty()) return;
        allCategories.forEach(category -> redisTemplate.<String, Category>opsForHash().put(cacheKey, category.getId(), category));
    }

    @Override
    public void processContext(ITreeChanger<Category> treeChanger) {
        // 相关节点数据
        Category current = treeChanger.current();
        Category currentParent = treeChanger.currentParent();
        Category beforeParent = treeChanger.beforeParent();
        // 全路径处理
        current.setFullPath(current.getId());

        // 父节点的子节点数 --
        if (null != treeChanger.beforeParent()) {
            treeChanger.beforeParent().minusChild();
        }
        // 新的父节点的孩子数 ++
        if (null != treeChanger.currentParent()) {
            treeChanger.currentParent().plusChild();
        }
        // 所有数据
        List<Category> family = treeChanger.descendants();
        // 子节点树层级处理
        family.forEach(cat -> cat.setLevel(cat.getLevel() + treeChanger.levelDelta()));
        family.add(current);
        // 验证树形层级
        for (Category category : family) {
            if (ProductConstants.LOW_CAT_LEVEL < category.getLevel()) {
                throw new ParamException("商品分类只允许三个层级");
            }
        }

        // 旧的父节点
        if (null != beforeParent) {
            family.add(beforeParent);
        }
        // 新的父节点
        if (null != currentParent) {
            family.add(currentParent);
            // 全路径处理
            current.setFullPath(currentParent.getFullPath() + IStrPool.CARET + current.getId());
        }
        // 所有数据更新
        for (Category category : family) {
            // 树形层级 + 树形级变化量
            boolean isOk = this.updateById(category);
            // 不成功回滚
            if (!isOk) throw new RollBackException();
        }
    }

    @Override
    public List<Category> findFamily(Category category) {
        List<Category> family = new ArrayList<>();
        family.add(category);

        // 顶级分类查询全家桶
        if (category.getLevel() == 1) {
            List<Category> children = this.listChildren(category.getId());
            // 没有子类
            if (children.isEmpty()) return family;
            // 孩子节点
            family.addAll(children);
            // 孙子类查询
            for (Category child : children) {
                // 孙节点
                List<Category> grandson = this.listChildren(child.getId());
                if (!grandson.isEmpty()) {
                    // 孙子节点（商品最多三级层级）
                    family.addAll(grandson);
                }
            }
            return family;
        }

        // 第二级分类查询全家桶
        if (category.getLevel() == 2) {
            // 查询父类
            Category parent = this.findWithCache(category.getPid());
            family.add(parent);

            // 查询子类
            List<Category> children = this.listChildren(category.getId());
            if (!children.isEmpty()) family.addAll(children);
            return family;
        }

        // 第三级分类
        if (category.getLevel() == ProductConstants.LOW_CAT_LEVEL) {
            // 父类分类查询
            Category parent = this.findWithCache(category.getPid());
            family.add(parent);
            // 顶级分类
            Category topCat = this.findWithCache(parent.getPid());
            family.add(topCat);
            return family;
        }
        throw new ParamException(ProductResult.CAT_ERROR);
    }

    @Override
    public void deleteAfterHandler(Category parentCategory) {
        if (null == parentCategory) return;
        parentCategory.minusChild();
        this.updateById(parentCategory);
    }
}
