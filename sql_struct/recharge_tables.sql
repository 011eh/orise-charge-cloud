-- ----------------------------
-- Table structure for omind_wallet
-- ----------------------------
DROP TABLE IF EXISTS `omind_wallet`;
create table omind_wallet (
    id                      bigint unsigned auto_increment comment '主键ID' primary key,
    user_id                 bigint unsigned                                                  not null comment '用户ID',
    balance                 decimal(10, 2)                         default 0.00              not null comment '钱包余额',
    recharge_balance        decimal(10, 2)                         default 0.00              not null comment '可用余额(不含赠送金额)',
    gift_balance            decimal(10, 2)                         default 0.00              not null comment '赠送金额',
    frozen_balance          decimal(10, 2)                         default 0.00              not null comment '冻结金额',
    frozen_recharge_balance decimal(10, 2)                         default 0.00              not null comment '冻结可用金额',
    frozen_gift_balance     decimal(10, 2)                         default 0.00              not null comment '冻结赠送金额',
    total_recharge          decimal(10, 2)                         default 0.00              not null comment '累计充值金额',
    total_gift              decimal(10, 2)                         default 0.00              not null comment '累计获得赠送金额',
    total_consumed          decimal(10, 2)                         default 0.00              not null comment '累计消费金额',
    total_recharge_consumed decimal(10, 2)                         default 0.00              not null comment '累计消费金额',
    total_gift_consumed     decimal(10, 2)                         default 0.00              not null comment '累计消费赠送金额',
    status                  tinyint(1)                             default 1                 not null comment '钱包状态：0-禁用，1-启用',
    version                 int                                    default 0                 not null comment '乐观锁版本号',
    remark                  varchar(128)                           default ''                null comment '备注',
    del_flag                tinyint unsigned                       default '0'               not null comment '删除标志（0代表存在 1代表删除）',
    create_dept             bigint unsigned                                                  null comment '创建部门',
    create_by               bigint unsigned                                                  null comment '创建者',
    create_time             datetime                               default current_timestamp not null comment '创建时间',
    update_by               bigint unsigned                                                  null comment '更新者',
    update_time             datetime                               default current_timestamp null on update current_timestamp comment '更新时间',
    tenant_id               varchar(20) collate utf8mb4_general_ci default '000000'          null comment '租户编号',
    constraint idx_user_id unique (user_id)
) comment '用户钱包表' collate = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for omind_recharge_package
-- ----------------------------
DROP TABLE IF EXISTS `omind_recharge_package`;
CREATE TABLE `omind_recharge_package` (
  `package_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '套餐ID',
  `package_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐名称',
  `recharge_money` decimal(10, 2) NOT NULL COMMENT '充值金额',
  `gift_money` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '赠送金额',
  `enable_state` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-正常，1-删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`package_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值套餐表';

-- ----------------------------
-- Table structure for omind_balance_flow
-- ----------------------------
DROP TABLE IF EXISTS `omind_balance_flow`;
create table omind_balance_flow (
    id               bigint unsigned auto_increment comment '流水ID' primary key,
    user_id          bigint unsigned                                                  not null comment '用户ID',
    transaction_type tinyint                                                          not null comment '交易类型：1-充值，2-消费，3-退款（提现）',
    amount           decimal(10, 2)                                                   not null comment '金额',
    gift_amount      decimal(10, 2)                         default 0.00              not null comment '赠送金额',
    status           tinyint                                default 1                 not null comment '状态(0:失败,1:成功,2:处理中)',
    balance_before   decimal(10, 2)                                                   not null comment '交易前余额',
    balance_after    decimal(10, 2)                                                   not null comment '交易后余额',
    available_before decimal(10, 2)                         default 0.00              not null comment '交易前可用余额',
    available_after  decimal(10, 2)                         default 0.00              not null comment '交易后可用余额',
    gift_before      decimal(10, 2)                         default 0.00              not null comment '交易前赠送余额',
    gift_after       decimal(10, 2)                         default 0.00              not null comment '交易后赠送余额',
    transaction_no   varchar(64)                            default ''                not null comment '交易流水号',
    out_trade_no     varchar(64)                            default ''                null comment '外部交易单号',
    related_id       varchar(64)                            default ''                null comment '关联ID，如订单ID、充电ID等',
    remark           varchar(128)                           default ''                null comment '备注',
    del_flag         tinyint unsigned                       default '0'               not null comment '删除标志（0代表存在 1代表删除）',
    create_dept      bigint unsigned                                                  null comment '创建部门',
    create_by        bigint unsigned                                                  null comment '创建者',
    create_time      datetime                               default current_timestamp not null comment '创建时间',
    update_by        bigint unsigned                                                  null comment '更新者',
    update_time      datetime                               default current_timestamp null on update current_timestamp comment '更新时间',
    tenant_id        varchar(20) collate utf8mb4_general_ci default '000000'          null comment '租户编号',
    constraint idx_transaction_no unique (transaction_no)
) comment '余额流水表' collate = utf8mb4_unicode_ci

-- ----------------------------
-- Table structure for omind_recharge_order
-- ----------------------------
DROP TABLE IF EXISTS `omind_recharge_order`;
CREATE TABLE `omind_recharge_order` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `package_id` bigint UNSIGNED NOT NULL COMMENT '套餐ID',
  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户订单号',
  `recharge_money` decimal(10, 2) NOT NULL COMMENT '充值金额',
  `gift_money` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '赠送金额',
  `pay_channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付渠道：WXPAY-微信支付，ALIPAY-支付宝',
  `trade_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '交易类型：JSAPI,APP等',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付，1-支付成功，2-支付失败，3-已取消',
  `transaction_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '支付平台交易号',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `refund_status` tinyint NOT NULL DEFAULT 0 COMMENT '退款状态：0-未退款，1-部分退款，2-全额退款',
  `refund_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '已退款金额',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_out_trade_no` (`out_trade_no`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值订单表';

DROP TABLE IF EXISTS `omind_withdrawal_record`;
CREATE TABLE `omind_withdrawal_record` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '提现记录ID',
    `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
    `out_refund_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户退款单号',
    `transaction_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '微信支付订单号',
    `refund_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '微信退款单号',
    `amount` decimal(10, 2) NOT NULL COMMENT '提现金额',
    `currency` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CNY' COMMENT '货币类型',
    `status` tinyint NOT NULL DEFAULT 0 COMMENT '提现状态：0-处理中，1-提现成功，2-提现失败',
    `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ORIGINAL' COMMENT '退款渠道：ORIGINAL-原路退款，BALANCE-退回余额',
    `reason` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '提现原因',
    `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `success_time` datetime DEFAULT NULL COMMENT '成功时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
    `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_out_refund_no` (`out_refund_no`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提现记录表'; 
