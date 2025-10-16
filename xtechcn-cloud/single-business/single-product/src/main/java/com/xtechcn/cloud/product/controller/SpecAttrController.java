package com.xtechcn.cloud.product.controller;

import com.xtechcn.cloud.product.constants.CacheConstants;
import com.xtechcn.cloud.product.constants.ProductConstants;
import com.xtechcn.cloud.product.constants.ProductResult;
import com.xtechcn.cloud.product.entity.Category;
import com.xtechcn.cloud.product.entity.SpecName;
import com.xtechcn.cloud.product.model.SpecAttrModel;
import com.xtechcn.cloud.product.model.po.SpecNameParam;
import com.xtechcn.cloud.product.service.CategoryService;
import com.xtechcn.cloud.product.service.SpecNameService;
import com.xtechcn.cloud.product.service.SpuSpecValueService;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.log.annotation.SysLog;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
* SPU销售属性
*
* @author hanjie
* @since 2025-09-16 09:45:55
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/attr/space")
@Tag(description = "Attr-Spec", name = "属性--规格属性")
public class SpecAttrController {

    private final CategoryService categoryService;
    private final SpecNameService specNameService;
    private final SpuSpecValueService spuSpecValueService;

    @GetMapping(value = "/cat3/list")
    @Operation(summary = "根据三级分类查询规格", description = "根据三级分类查询规格")
    public R listByCat3(@RequestParam(value = "cat3Id") String cat3Id) {
        Category category = categoryService.findWithCache(cat3Id);
        if (null == category || ProductConstants.LOW_CAT_LEVEL != category.getLevel()) {
            // 只允许三级分类查询
            return R.failed(ProductResult.CAT_LEVEL_ILLEGAL);
        }
        List<SpecName> specNames = specNameService.listByCat3IdWithCache(cat3Id);
        // 规格属性不存在
        if (specNames.isEmpty()) return R.ok(new ArrayList<>());
        List<SpecAttrModel> specAttrModels = specNameService.convertSpecAttrModels(specNames);
        return R.ok(specAttrModels);
    }

    @SysLog(value = "新增/修改规格名")
    @PostMapping(value = "/name/upsert")
    @PreAuthorize("@pmh.hasPermission('goods:cat:upsert')")
    @Operation(summary = "新增/修改规格名", description = "新增/修改规格名")
    @CacheEvict(value = CacheConstants.SPEC_ATTR_NAME_CAT_CACHE, key = "#specNameParam.cat3Id()", allEntries = true)
    public R upsertName(@RequestBody @Valid SpecNameParam specNameParam) {
        Category category = categoryService.findWithCache(specNameParam.cat3Id());
        if (null == category || ProductConstants.LOW_CAT_LEVEL != category.getLevel()) {
            // 只允许三级分类查询
            return R.failed(ProductResult.CAT_LEVEL_ILLEGAL);
        }
        SpecName specName = SpecName.create(specNameParam.cat3Id(), specNameParam.nameId(), specNameParam.specName());
        // 数据唯一性验证
        specNameService.uniqueCheck(specName);
        boolean isOk = specNameParam.isAdd() ? specNameService.save(specName) : specNameService.updateById(specName);
        return R.ok(isOk);
    }

    @SysLog(value = "移除规格名")
    @PreAuthorize("@pmh.hasPermission('goods:cat:del')")
    @DeleteMapping(value = "/name/remove/{nameId}")
    @Operation(summary = "移除规格名", description = "移除规格名")
    public R removeName(@PathVariable(value = "nameId") Long nameId) {
        SpecName specName = specNameService.getById(nameId);
        List<SpecName> specNames = specNameService.listByCat3IdWithCache(specName.getCat3Id());
        if (specNames.size() <= 1) {
            // 规格组下至少保留一个属性
            return R.failed(ProductResult.SPEC_NAME_KEEP_LEAST_ONE);
        }
        // 删除规格组下所有的规格名称
        boolean isOk = specNameService.removeWithCache(specName);
        if (isOk) {
            // 删除SPU 关联的规格组下的规格值
            spuSpecValueService.removeByName(nameId);
        }
        return R.ok(isOk);
    }
}