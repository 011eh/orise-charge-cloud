package org.dromara.omind.userplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.omind.userplat.api.domain.dto.OmindRechargePackageDto;
import org.dromara.omind.userplat.api.domain.entity.OmindRechargePackageEntity;
import org.dromara.omind.userplat.service.OmindRechargePackageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 充值套餐Controller
 */
@Tag(name = "充值套餐接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/userplat/recharge/package")
public class OmindRechargePackageController extends BaseController {

    private final OmindRechargePackageService rechargePackageService;

    /**
     * 获取充值套餐列表
     */
    @Operation(summary = "获取充值套餐列表")
    @GetMapping("/list")
    public R<List<OmindRechargePackageDto>> getRechargePackageList() {
        return R.ok(rechargePackageService.getRechargePackageList());
    }

    /**
     * 获取充值套餐列表（后台管理）
     */
    @Operation(summary = "获取充值套餐列表（后台管理）")
    @GetMapping("/list/admin")
    public TableDataInfo<OmindRechargePackageDto> getRechargePackageListForAdmin(OmindRechargePackageDto packageDto, PageQuery pageQuery) {
        return rechargePackageService.getRechargePackageListForAdmin(packageDto, pageQuery);
    }

    /**
     * 获取充值套餐详情
     */
    @Operation(summary = "获取充值套餐详情")
    @GetMapping("/detail")
    public R<OmindRechargePackageDto> getRechargePackageDetail(@Parameter(description = "套餐ID") @RequestParam Long packageId) {
        return R.ok(rechargePackageService.getRechargePackageById(packageId));
    }

    /**
     * 新增充值套餐
     */
    @Operation(summary = "新增充值套餐")
    @PostMapping
    public R<Boolean> addRechargePackage(@RequestBody OmindRechargePackageEntity packageEntity) {
        return R.ok(rechargePackageService.addRechargePackage(packageEntity));
    }

    /**
     * 修改充值套餐
     */
    @Operation(summary = "修改充值套餐")
    @PutMapping
    public R<Boolean> updateRechargePackage(@RequestBody OmindRechargePackageEntity packageEntity) {
        return R.ok(rechargePackageService.updateRechargePackage(packageEntity));
    }

    /**
     * 删除充值套餐
     */
    @Operation(summary = "删除充值套餐")
    @DeleteMapping
    public R<Boolean> deleteRechargePackage(@Parameter(description = "套餐ID") @RequestParam @NotNull(message = "套餐ID不能为空") Long packageId) {
        return R.ok(rechargePackageService.deleteRechargePackage(packageId));
    }
} 