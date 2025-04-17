package org.dromara.omind.userplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.omind.userplat.domain.entity.OmindWithdrawalRecordEntity;
import org.dromara.omind.userplat.service.OmindWalletService;
import org.dromara.omind.userplat.service.OmindWithdrawalService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 提现管理
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/userplat/wallet/withdrawal")
@Tag(name = "提现管理API")
public class OmindWithdrawalController extends BaseController {

    private final OmindWalletService walletService;
    private final OmindWithdrawalService withdrawalService;

    /**
     * 申请提现
     */
    @Operation(summary = "申请提现")
    @PostMapping("/apply")
    public R<Long> applyWithdrawal(@Parameter(description = "用户ID") @RequestParam Long userId,
                                @Parameter(description = "提现金额") @RequestParam BigDecimal amount,
                                @Parameter(description = "提现原因") @RequestParam(required = false) String reason) {
        return R.ok(withdrawalService.applyWithdrawal(userId, amount, reason));
    }

    /**
     * 获取提现记录
     */
    @Operation(summary = "获取提现记录")
    @GetMapping("/list")
    public TableDataInfo<OmindWithdrawalRecordEntity> listWithdrawalRecords(@Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
                                                                            PageQuery pageQuery) {
        return withdrawalService.getWithdrawalRecords(userId, pageQuery);
    }

    /**
     * 提现回调通知
     */
    @Operation(summary = "提现回调通知")
    @PostMapping("/callback")
    public R<Boolean> handleCallback(@RequestBody WithdrawalCallbackParam param) {
        return R.ok(withdrawalService.handleWithdrawalCallback(param.getOutRefundNo(), param.getRefundId(), param.getStatus()));
    }

    /**
     * 回调参数
     */
    public static class WithdrawalCallbackParam {
        private String outRefundNo;
        private String refundId;
        private String status;

        public String getOutRefundNo() {
            return outRefundNo;
        }

        public void setOutRefundNo(String outRefundNo) {
            this.outRefundNo = outRefundNo;
        }

        public String getRefundId() {
            return refundId;
        }

        public void setRefundId(String refundId) {
            this.refundId = refundId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
} 
