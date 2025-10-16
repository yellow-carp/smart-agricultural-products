package com.xtechcn.cloud.customer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtechcn.cloud.customer.entity.Unit;
import com.xtechcn.cloud.customer.model.po.CarParam;
import com.xtechcn.cloud.customer.model.vo.CarPageView;
import com.xtechcn.common.core.lang.Disenable;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.result.R;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.log.annotation.SysLog;
import com.xtechcn.cloud.customer.entity.Car;
import com.xtechcn.cloud.customer.service.CarService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 车辆管理
 *
 * @author hanjie
 * @since 2025-09-11 17:59:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/car")
@Tag(description = "car", name = "车辆管理")
public class CarController {

    private final CarService carService;

    @GetMapping(value = "/page")
    @Operation(description = "分页查询", summary = "分页查询")
    @PreAuthorize("@pmh.hasPermission('ct:car:r')")
    public R<Page<CarPageView>> pageQuery(Page page, Car car) {
        page = carService.page(page, Wrappers.lambdaQuery(car));
        List<Car> cars = page.getRecords();
        if (ICollUtil.isNotEmpty(cars)) {
            // 封装数据
            List<CarPageView> carPageViewList = carService.returnViewHandler(cars);
            page.setRecords(carPageViewList);
        }
        return R.ok(page);
    }

    @SysLog("新增修改车辆管理")
    @PostMapping(value = "/upsert")
    @Operation(description = "新增车辆管理", summary = "新增车辆管理")
    @PreAuthorize("@pmh.hasPermission('ct:car:w')")
    public R save(@RequestBody @Valid CarParam carAddParam) {
        // 参数转实体
        Car car = carService.conventEntity(carAddParam);
        // 重复验证
        carService.uniqueCheck(car);
        // 初始化 重新修改最大最小货物件数
        carService.updateNum(car);
        boolean isOk = carAddParam.isAdd() ? carService.save(car) : carService.updateById(car);
        return R.ok(isOk);
    }

    @PostMapping(value = "/disenable")
    @Operation(description = "启停车辆", summary = "启停车辆")
    @PreAuthorize("@pmh.hasPermission('ct:car:w')")
    public R<Boolean> disenable(@RequestBody Disenable disenable) {
        Car car = carService.findAndCache(disenable.ofId());
        if (null ==  car) return R.failed(FailedResult.NOT_FOUNT);
        if (Objects.equals(car.getEnabled(), disenable.enabled())){
            return R.ok(true);
        }
        boolean isOk = carService.updateById(car.withId().enable(disenable.enabled()));
        return R.ok(isOk);
    }

    @SysLog("通过id删除车辆管理")
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("@pmh.hasPermission('ct:car:d')")
    @Operation(description = "指定id删除车辆管理", summary = "指定id删除车辆管理")
    public R removeById(@PathVariable String id) {
        boolean isOk = carService.removeById(id);
        // 删除缓存
        carService.removeCache(id);
        return R.ok(isOk);
    }

}