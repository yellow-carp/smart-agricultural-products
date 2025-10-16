package com.xtechcn.cloud.product.controller;

import com.xtechcn.cloud.product.constants.ProductConstants;
import com.xtechcn.cloud.product.constants.ProductResult;
import com.xtechcn.cloud.product.model.CategoryQuery;
import com.xtechcn.cloud.product.model.CategoryTree;
import com.xtechcn.cloud.product.model.po.CategoryParam;
import com.xtechcn.cloud.product.service.SpecNameService;
import com.xtechcn.common.core.constants.TreeConstant;
import com.xtechcn.common.core.lang.Disenable;
import com.xtechcn.common.core.lang.tree.ITreeChanger;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.product.entity.Category;
import com.xtechcn.cloud.product.service.CategoryService;
import com.xtechcn.common.mp.wrappers.IWrappers;
import com.xtechcn.common.security.annotation.OpenApi;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 商品分类
 *
 * @author hanjie
 * @since 2025-09-16 09:45:56
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@Tag(description = "category", name = "商品分类")
public class CategoryController {

    private final CategoryService categoryService;
    private final SpecNameService specNameService;

    @OpenApi
    @GetMapping(value = "/portal/tree")
    @Operation(summary = "小程序-门户分类树", description = "小程序-门户分类树")
    public R portalTree() {
        // 查询所有的商品分类数据
        List<CategoryTree> categoryTrees = categoryService.activeCategoryTree();
        return R.ok(categoryTrees);
    }

    @GetMapping(value = "/allTree")
    @Operation(summary = "分类总树", description = "分类总树")
    public R allTree() {
        // 查询所有的商品分类数据
        List<CategoryTree> categoryTrees = categoryService.allCategoryTree();
        return R.ok(categoryTrees);
    }

    @GetMapping(value = "/activeTree")
    @Operation(summary = "激活的分类树", description = "激活的分类树")
    public R activeTree() {
        // 查询所有的商品分类数据
        List<CategoryTree> categoryTrees = categoryService.activeCategoryTree();
        return R.ok(categoryTrees);
    }

    @GetMapping(value = "/list")
    @Operation(summary = "list查询", description = "list查询")
    @PreAuthorize("@pmh.hasPermission('goods:cat:view')")
    public R pageQuery(CategoryQuery categoryQuery) {
        List<Category> categories = categoryService.list(IWrappers.<Category>lambdaQuery(categoryQuery).orderByAsc(Category::getSort).orderByDesc(Category::getUpsertTime));
        List<CategoryTree> categoryTrees = new ArrayList<>();
        if (ICollUtil.isNotEmpty(categories)) {
            categoryTrees = categoryService.returnViewHandler(categories);
        }
        return R.ok(categoryTrees);
    }

    @Transactional
    @SysLog("新增分类")
    @PostMapping(value = "/add")
    @PreAuthorize("@pmh.hasPermission('goods:cat:upsert')")
    @Operation(summary = "新增分类", description = "新增分类")
    public R add(@RequestBody CategoryParam categoryParam) {
        Category category = categoryService.convertEntity(categoryParam);
        // 分类父级
        Category parent = TreeConstant.TREE_ROOT_ID.equals(category.getPid()) ? null : categoryService.getById(category.getPid());
        // 数据插入初始化处理
        categoryService.insertInit(category, parent);
        // 数据唯一性验证
        categoryService.uniqueCheck(category);
        // 数据入库
        boolean isOk = categoryService.save(category);
        if (isOk) {
            // 父级处理
            categoryService.insertAfterHandler(category, parent);
            // 添加缓存
            categoryService.addCache(category);
        }
        return R.ok(true);
    }

    @Transactional
    @SysLog("修改分类")
    @PutMapping(value = "/update")
    @PreAuthorize("@pmh.hasPermission('goods:cat:upsert')")
    @Operation(summary = "修改分类", description = "修改分类")
    public R update(@RequestBody CategoryParam categoryParam) {
        Category category = categoryService.convertEntity(categoryParam);
        // 数据修改初始化
        categoryService.updateInit(category);
        // 数据唯一性验证
        categoryService.uniqueCheck(category);
        // 变更前数据
        Category beforeCategory = categoryService.getById(category.getId());
        // 父类没有变化,知识普通的基本信息数据修改
        if (Objects.equals(category.getPid(), beforeCategory.getPid())) {
            // 入库更新
            boolean isOk = categoryService.updateById(category);
            // 刷新缓存
            categoryService.refreshCache();
            return R.ok(isOk);
        }

        // 如果原来是三级分类,则需要判断分类下是否已经挂载数据
        if (ProductConstants.LOW_CAT_LEVEL == beforeCategory.getLevel()) {
            boolean hasAttrs = specNameService.cat3IdHasSpecName(beforeCategory.getId());
            // 三级分类Id已经存在规格属性数据挂载,不允许修改层级
            if (hasAttrs) return R.failed(ProductResult.CAT_HAS_PRODUCT);
        }
        // 父级发生了变更
        ITreeChanger<Category> treeChanger = ITreeChanger.<Category>create().current(category).before(beforeCategory);

        // 原分类父级
        Category beforeParent = TreeConstant.TREE_ROOT_ID.equals(beforeCategory.getPid()) ? null : categoryService.getById(beforeCategory.getPid());
        treeChanger.beforeParent(beforeParent);
        // 新的父类处理
        Category currentParent = TreeConstant.TREE_ROOT_ID.equals(category.getPid()) ? null : categoryService.getById(category.getPid());
        treeChanger.currentParent(currentParent);
        // 分类树的层级变化量
        int level = null == currentParent ? TreeConstant.TREE_TOP_LEVEL : currentParent.getLevel() + 1;
        treeChanger.current().setLevel(level);
        // 孩子节点数据
        List<Category> descendants = categoryService.listChildren(category);
        if (!descendants.isEmpty()) {
            for (Category child : descendants) {
                // 孙节点
                List<Category> grandson = categoryService.listChildren(child.getId());
                if (!grandson.isEmpty()) {
                    descendants.addAll(grandson);
                }
            }
        }
        treeChanger.descendants(descendants);
        treeChanger.levelDelta(category.getLevel() - beforeCategory.getLevel());
        categoryService.processContext(treeChanger);
        // 刷新缓存
        categoryService.refreshCache();
        return R.ok(true);
    }

    @Transactional
    @PutMapping(value = "/disenable")
    @Operation(summary = "停用/启用", description = "停用/启用")
    @PreAuthorize("@pmh.hasPermission('goods:cat:upsert')")
    public R<Boolean> disEnable(@RequestBody Disenable disenable) {
        Category category = categoryService.getById(disenable.ofId());
        // 当前的状态已经是要变更的状态
        if (category.getEnabled().equals(disenable.enabled())) {
            return R.ok();
        }

        // 商品分类家族
        List<Category> family = categoryService.findFamily(category);
        Map<Integer, List<Category>> group = IMapUtil.group(family, Category::getLevel);
        if (group.size() != 3) {
            return R.failed(ProductResult.CAT_TREE_INCOMPLETE);
        }

        if (disenable.enabled()) {
            // 启用的时候相关联的家族成员都需要启用
            family.forEach(cat -> cat.setEnabled(true));
        } else {
            // 使用switch击穿进行完成
            switch (category.getLevel()) {
                case 1:
                    group.get(1).forEach(cat -> cat.setEnabled(false));
                case 2:
                    group.get(2).forEach(cat -> cat.setEnabled(false));
                case 3:
                    group.get(3).forEach(cat -> cat.setEnabled(false));
            }
        }
        boolean isOk = categoryService.updateBatchById(family);
        // 刷新缓存
        categoryService.refreshCache();
        return R.ok(isOk);
    }

    @PutMapping(value = "/refresh")
    @Operation(summary = "刷新缓存", description = "刷新缓存")
    @PreAuthorize("@pmh.hasPermission('goods:cat:refresh')")
    public R refreshCache() {
        categoryService.refreshCache();
        return R.ok(true);
    }

    @SysLog(value = "删除分类")
    @DeleteMapping(value = "/remove/{catId}")
    @PreAuthorize("@pmh.hasPermission('goods:cat:del')")
    @Operation(summary = "删除分类", description = "删除分类")
    public R remove(@PathVariable(value = "catId") Long catId) {
        Category category = categoryService.getById(catId);
        if (category.getHasChildren()) {
            // 不允许删除存在子节点的分类
            return R.failed(ProductResult.CAT_HAS_CHILD);
        }
        // 三级分类编码需要查询其挂载的商品数量
        if (ProductConstants.LOW_CAT_LEVEL == category.getLevel()) {
            boolean cat3IdHasAttss = specNameService.cat3IdHasSpecName(category.getId());
            // 三级分类Id已经存在规格属性数据挂载,不允许修改层级
            if (cat3IdHasAttss) return R.failed(ProductResult.CAT_HAS_PRODUCT);
        }
        // 执行删除
        boolean isOk = categoryService.removeById(category.getId());

        if (isOk) {
            Category parentCategory = TreeConstant.TREE_ROOT_CODE.equals(category.getPid()) ? null : categoryService.getById(category.getPid());
            // 删除菜单处理
            categoryService.deleteAfterHandler(parentCategory);
            // 清除缓存
            categoryService.refreshCache();
        }
        return R.ok(isOk);
    }
}