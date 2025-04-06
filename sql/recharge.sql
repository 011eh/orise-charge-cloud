-- ----------------------------
-- Table structure for omind_wallet
-- ----------------------------
DROP TABLE IF EXISTS `omind_wallet`;
CREATE TABLE `omind_wallet` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '钱包余额',
  `frozen_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '冻结金额',
  `total_recharge` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计充值金额',
  `total_consume` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计消费金额',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '钱包状态：0-禁用，1-启用',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint UNSIGNED NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint UNSIGNED NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户钱包表';

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
  `create_dept` bigint UNSIGNED NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint UNSIGNED NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`package_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值套餐表';

-- ----------------------------
-- Table structure for omind_balance_flow
-- ----------------------------
DROP TABLE IF EXISTS `omind_balance_flow`;
CREATE TABLE `omind_balance_flow` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `flow_type` tinyint NOT NULL COMMENT '流水类型：1-充值，2-消费，3-退款',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `balance` decimal(10, 2) NOT NULL COMMENT '变动后余额',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '交易单号',
  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '外部交易单号',
  `related_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '关联ID，如订单ID、充电ID等',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint UNSIGNED NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint UNSIGNED NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_trade_no` (`trade_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='余额流水表';

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
  `create_dept` bigint UNSIGNED NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint UNSIGNED NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_out_trade_no` (`out_trade_no`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值订单表';

-- 添加示例充值套餐数据
INSERT INTO `omind_recharge_package` (`package_name`, `recharge_money`, `gift_money`, `enable_state`, `is_del`)
VALUES 
('50元套餐', 50.00, 0.00, 1, 0),
('100元套餐', 100.00, 5.00, 1, 0),
('200元套餐', 200.00, 15.00, 1, 0),
('500元套餐', 500.00, 50.00, 1, 0),
('1000元套餐', 1000.00, 150.00, 1, 0); 