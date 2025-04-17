package org.dromara.omind.userplat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("omind_withdrawal_record")
public class OmindWithdrawalRecordEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String outRefundNo;
    
    private String transactionId;
    
    private String refundId;
    
    private BigDecimal amount;
    
    private String currency;
    
    private Integer status;
    
    private String channel;
    
    private String reason;
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime successTime;
    
    private LocalDateTime updateTime;
    
    private Integer delFlag;
    
    private String tenantId;
} 
