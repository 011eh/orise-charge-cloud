package org.dromara.omind.userplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.omind.userplat.api.domain.dto.OmindBalanceFlowDto;
import org.dromara.omind.userplat.api.domain.dto.OmindWalletDto;
import org.dromara.omind.userplat.service.OmindWalletService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 钱包管理Controller
 */
@Tag(name = "钱包管理接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/userplat/wallet")
public class OmindWalletController extends BaseController {

    private final OmindWalletService walletService;

    /**
     * 获取用户钱包信息
     */
    @Operation(summary = "获取用户钱包信息")
    @GetMapping("/info")
    public R<OmindWalletDto> getUserWallet(@Parameter(description = "用户ID") @RequestParam Long userId) {
        return R.ok(walletService.getUserWallet(userId));
    }

    /**
     * 获取余额流水记录
     */
    @Operation(summary = "获取余额流水记录")
    @GetMapping("/flow/list")
    public TableDataInfo<OmindBalanceFlowDto> getBalanceFlowList(OmindBalanceFlowDto balanceFlowDto, PageQuery pageQuery) {
        return walletService.getBalanceFlowList(balanceFlowDto, pageQuery);
    }

    /**
     * 创建充值订单
     */
    @Operation(summary = "创建充值订单")
    @PostMapping("/recharge")
    public R<String> recharge(@Parameter(description = "用户ID") @RequestParam @NotNull(message = "用户ID不能为空") Long userId,
                              @Parameter(description = "套餐ID") @RequestParam @NotNull(message = "套餐ID不能为空") Long packageId,
                              @Parameter(description = "支付渠道: WXPAY-微信支付，ALIPAY-支付宝") @RequestParam @NotNull(message = "支付渠道不能为空") String payChannel,
                              @Parameter(description = "交易类型: JSAPI,APP等") @RequestParam @NotNull(message = "交易类型不能为空") String tradeType) {
        // 通过套餐ID，调用套餐服务获取套餐金额和赠送金额信息，先简化处理，实际应在service中处理
        return R.ok(walletService.recharge(userId, packageId, null, null, payChannel, tradeType));
    }

    /**
     * 消费钱包余额
     */
    @Operation(summary = "消费钱包余额")
    @PostMapping("/consume")
    public R<Boolean> consume(@RequestBody ConsumeRequest request) {
        return R.ok(walletService.consume(request.getUserId(), request.getAmount(), request.getRelatedId(), request.getRemark()));
    }

    /**
     * 消费请求参数
     */
    @Data
    @NoArgsConstructor
    public static class ConsumeRequest {
        /**
         * 用户ID
         */
        @NotNull(message = "用户ID不能为空")
        private Long userId;

        /**
         * 消费金额
         */
        @NotNull(message = "消费金额不能为空")
        private BigDecimal amount;

        /**
         * 关联ID，如订单ID、充电ID等
         */
        private String relatedId;

        /**
         * 备注
         */
        private String remark;
    }
} 
