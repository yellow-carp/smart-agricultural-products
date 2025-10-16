package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.model.po.PostAddressParam;
import com.xtechcn.cloud.customer.model.vo.CarPageView;
import com.xtechcn.cloud.customer.model.vo.PostAddressView;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.PostAddress;
import com.xtechcn.cloud.customer.service.PostAddressService;
import com.xtechcn.common.security.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮递地址信息表
 *
 * @author hanjie
 * @since 2025-09-11 17:59:28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
@Tag(description = "postaddress", name = "邮递地址信息表")
public class PostAddressController {

    private final PostAddressService postAddressService;


    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:postaddress:r')")
    public R<Page<PostAddressView>> pageQuery(Page page, PostAddress postAddress) {
        page = postAddressService.page(page, Wrappers.lambdaQuery(postAddress));
        if (ICollUtil.isNotEmpty(page.getRecords())) {
            List<PostAddress> postAddressList = page.getRecords();
            // 封装数据
            List<PostAddressView> postAddressViewList = postAddressService.returnViewHandler(postAddressList);
            page.setRecords(postAddressViewList);
        }
        return R.ok(page);
    }


    @GetMapping(value = "/details/{id}")
    @Operation(description = "指定Id查询", summary = "指定Id查询")
    @PreAuthorize("@pmh.hasPermission('ct:postaddress:r')")
    public R<PostAddress> getById(@PathVariable("id") String id) {
        // 这里实施示例,没有特殊的 Vo 返回封装需求,这里请直接移除该接口
        PostAddress postAddress = postAddressService.getById(id);
        return R.ok(postAddress);
    }


    @SysLog("新增修改邮递地址信息表")
    @PostMapping(value = "/upsert")
    @Operation(description = "新增邮递地址信息表", summary = "新增邮递地址信息表")
    @PreAuthorize("@pmh.hasPermission('ct:postaddress:w')")
    public R save(@RequestBody @Valid PostAddressParam postAddressParam) {
        // 参数转实体
        PostAddress postAddress = postAddressService.conventEntity(postAddressParam);
        // 唯一性校验
        postAddressService.uniqueCheck(postAddress);
        // todo 采购商收货地址范围校验，地址只能在采购商所在销区
        boolean isOk = postAddressParam.isAdd() ? postAddressService.save(postAddress) : postAddressService.updateById(postAddress);
        return R.ok(isOk);
    }


    @SysLog("通过id删除邮递地址信息表")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('ct:postaddress:d')")
    @Operation(description = "指定id删除邮递地址信息表", summary = "指定id删除邮递地址信息表")
    public R removeById(@PathVariable String id) {
        PostAddress postAddress = postAddressService.getById(id);
        if (postAddress == null) return R.ok(true);
        boolean isOk = postAddressService.removeById(id);
        return R.ok(isOk);
    }

}