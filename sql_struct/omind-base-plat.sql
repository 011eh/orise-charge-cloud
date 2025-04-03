/*
 Navicat Premium Data Transfer

 Source Server         : omind
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : omind-base-plat

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 03/04/2025 16:39:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for plat_connector_realtime_data
-- ----------------------------
DROP TABLE IF EXISTS `plat_connector_realtime_data`;
CREATE TABLE `plat_connector_realtime_data`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增字段',
  `trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '交易流水号32位；',
  `connector_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电设备接口编码，同一运营商内唯一；组织机构9位+桩14+枪3，补0',
  `pile_no` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '桩编号',
  `gun_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '枪编号',
  `state` tinyint(1) NULL DEFAULT 0 COMMENT '0离线 1故障 2空闲 3充电',
  `gun_state` tinyint(1) NULL DEFAULT 2 COMMENT '枪是否归位 0否 1是 2未知',
  `gun_link` tinyint(1) NULL DEFAULT 0 COMMENT '是否插枪 0否 1是',
  `out_voltage` decimal(10, 1) NULL DEFAULT 0.0 COMMENT '输出电压 精确到小数后一位；待机置零',
  `out_current` decimal(10, 1) NULL DEFAULT 0.0 COMMENT '输出电流 精确到小数后一位；待机置零',
  `gunline_temp` int NULL DEFAULT 0 COMMENT '枪线温度 偏移量-50 待机置0',
  `gunline_no` bigint NULL DEFAULT 0 COMMENT '枪线编码 8位bin码',
  `soc` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '0-100 剩余电量',
  `battery_max_temp` int NULL DEFAULT 0 COMMENT '电池组最高温度 偏移量-50 待机置0',
  `total_charge_durant` int NULL DEFAULT 0 COMMENT '累计充电时间 单位Min，待机置零',
  `remain_charge_durant` int NULL DEFAULT 0 COMMENT '剩余充电时间 单位Min，待机置零',
  `charge_kwh` decimal(10, 4) NULL DEFAULT 0.0000 COMMENT '充电读数，精确到小数点后4位；待机置零',
  `lose_kwh` decimal(10, 4) NULL DEFAULT 0.0000 COMMENT '计损充电读书，精确到小数点后四位;待机置零 未设置计损比例时等于充电度数',
  `charge_money` decimal(10, 4) NULL DEFAULT 0.0000 COMMENT '精确到小数点后四位;待机置零 (电费+服务费)*计损充电度数',
  `hd_error` tinyint NULL DEFAULT 0 COMMENT '硬件故障，位运算解析',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `plat_connector_realtime_data_trade_no_IDX`(`trade_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3462884 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础平台数据——充电接口实时监测数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plat_trade_record
-- ----------------------------
DROP TABLE IF EXISTS `plat_trade_record`;
CREATE TABLE `plat_trade_record`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '交易流水号',
  `pile_no` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '桩编号',
  `gun_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '枪编号',
  `start_time` datetime NOT NULL COMMENT '开始充电时间',
  `end_time` datetime NOT NULL COMMENT '本次采样时间',
  `sharp_per_price` decimal(10, 5) NOT NULL DEFAULT 0.00000 COMMENT '尖单价',
  `sharp_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '尖电量',
  `sharp_all_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '记损尖电量',
  `sharp_price` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '尖金额',
  `peak_per_price` decimal(10, 5) NOT NULL DEFAULT 0.00000 COMMENT '峰单价',
  `peak_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '峰电量',
  `peak_all_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '计损峰电量',
  `peak_price` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '峰金额',
  `flat_per_price` decimal(10, 5) NOT NULL DEFAULT 0.00000 COMMENT '平单价',
  `flat_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '平电量',
  `flat_all_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '计损平电量',
  `flat_price` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '平金额',
  `valley_per_price` decimal(10, 5) NOT NULL DEFAULT 0.00000 COMMENT '谷单价',
  `valley_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '谷电量',
  `valley_all_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '计损谷电量',
  `valley_price` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '谷金额',
  `start_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '电表总起值',
  `end_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '电报总止值',
  `final_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '总电量',
  `final_all_kwh` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '总计损电量',
  `cost` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '消费金额',
  `vin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '车辆VIN码',
  `trade_type` tinyint NOT NULL DEFAULT 0 COMMENT '交易标识 0x01app启动 0x02卡启动 0x04离线卡启动 0x05 vin码启动充电',
  `trade_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易日期、时间',
  `stop_type` int NOT NULL DEFAULT 0 COMMENT '停止原因 ',
  `system_card_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物理卡号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10456 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_charge_order
-- ----------------------------
DROP TABLE IF EXISTS `sys_charge_order`;
CREATE TABLE `sys_charge_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `start_charge_seq` varchar(27) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电订单号：运营商ID+唯一编号 27个字符； 组织机构9位+id18位',
  `operator_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运营商ID',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '交易流水号（基础平台和桩的规则 32位 16位BCD）',
  `start_charge_seq_stat` tinyint NOT NULL DEFAULT 5 COMMENT '充电订单状态；1启动中 2充电中 3停止中 4已结束 5未知',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联充电站ID，运营商自定义的唯一编码 小于等于20字符',
  `connector_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电设备接口编码',
  `connector_status` int NOT NULL DEFAULT 1 COMMENT '1空闲 2占用（未充电） 3占用（充电中） 4占用（预约锁定） 255故障',
  `current_a` decimal(10, 1) NOT NULL DEFAULT 0.0 COMMENT 'A相电流 单位A 默认0，含直流（输出）',
  `current_b` decimal(10, 1) NULL DEFAULT 0.0 COMMENT 'B相电流',
  `current_c` decimal(10, 1) NULL DEFAULT 0.0 COMMENT 'C相电流',
  `voltage_a` decimal(10, 1) NOT NULL DEFAULT 0.0 COMMENT 'A相电压 单位V 默认0，含直流（输出）',
  `voltage_b` decimal(10, 1) NULL DEFAULT 0.0 COMMENT 'B相电压',
  `voltage_c` decimal(10, 1) NULL DEFAULT 0.0 COMMENT 'C相电压',
  `soc` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '电池剩余电量0-1.00',
  `start_time` datetime NOT NULL COMMENT '开始充电时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '最新采样时间',
  `total_power` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计充电量（度）',
  `elec_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '累计电费（元）',
  `service_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '累计服务费（元）',
  `total_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '累计总金额（元）',
  `sum_period` tinyint NULL DEFAULT 0 COMMENT '时段数0-32；实际约定为1-24时段，小时为单位',
  `fail_reason` int NULL DEFAULT 0 COMMENT '故障原因 0无 1此设备不存在 2此设备离线 3设备已停止充电 4-99自定义（参考12.1 充电停止原因代码表）',
  `car_vin` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'vin码',
  `sync_flag` tinyint(1) NULL DEFAULT 0 COMMENT '订单结束后是否已和用户平台同步 0否 1是',
  `report_gov` tinyint(1) NULL DEFAULT 0 COMMENT '是否已上报至市政平台',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记',
  `plate_num` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '车牌号',
  `phone_num` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `price_info` varchar(3200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单时计价',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_charge_order_trade_no_IDX`(`trade_no` ASC) USING BTREE,
  INDEX `sys_charge_order_start_charge_seq_IDX`(`start_charge_seq` ASC) USING BTREE,
  INDEX `sys_charge_order_connector_id_IDX`(`connector_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '充电订单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_charge_order_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_charge_order_item`;
CREATE TABLE `sys_charge_order_item`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `start_charge_seq` varchar(27) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电订单号',
  `start_time` datetime NOT NULL COMMENT '开始时间 yyyy-MM-dd HH:mm:ss',
  `end_time` datetime NOT NULL COMMENT '结束时间 yyyy-MM-dd HH:mm:ss',
  `elec_price` decimal(10, 4) NULL DEFAULT 0.0000 COMMENT '时段电价 小数点后4位',
  `service_price` decimal(10, 4) NULL DEFAULT 0.0000 COMMENT '时段服务费价格 小数点后4位',
  `power` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '时段充电量 度 小数点后2位',
  `elec_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '时段电费 小数点后2位',
  `service_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '时段服务费 小数点后2位',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_charge_order_item_start_charge_seq_IDX`(`start_charge_seq` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 126 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '充电订单计费信息明细表（分时段数组）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_connector
-- ----------------------------
DROP TABLE IF EXISTS `sys_connector`;
CREATE TABLE `sys_connector`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `connector_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电设备接口编码，同一运营商内唯一；9位组织机构编码+14桩编码+3枪号',
  `equipment_id` varchar(23) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的设备ID，对同一运营商保证唯一',
  `gun_no` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '枪号',
  `connector_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '充电设备接口名称',
  `connector_type` tinyint NOT NULL DEFAULT 0 COMMENT '1家用插座 2交流接口插座 3交流接口插头 4直流接口枪头 5无线充电座 6其他',
  `voltage_upper_limits` int NOT NULL DEFAULT 0 COMMENT '额定电压上限',
  `voltage_lower_limits` int NOT NULL DEFAULT 0 COMMENT '额定电压下限',
  `current_value` int NULL DEFAULT 0 COMMENT '额定电流',
  `power` decimal(10, 1) NULL DEFAULT 0.0 COMMENT '额定功率',
  `park_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '车位号',
  `national_standard` tinyint NOT NULL DEFAULT 0 COMMENT '国家标准 1:2011 2:2015',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0离网 1空闲 2占用（未充电）3占用（充电）4占用（预约锁定）255故障',
  `park_status` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '0未知 10空闲 50占用',
  `lock_status` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '0未知 10已解锁 50已上锁',
  `price_code` int UNSIGNED NULL DEFAULT 0 COMMENT '价格模版ID',
  `state` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '0正常 1故障',
  `ping_tm` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次心跳时间，超过40秒视为离线',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` bigint NULL DEFAULT 0 COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint NULL DEFAULT 0 COMMENT '更新人ID',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `connector_id`(`connector_id` ASC) USING BTREE COMMENT '充电接口编号索引',
  INDEX `sys_connector_equipment_id_IDX`(`equipment_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 124 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '充电设备接口信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_equipment
-- ----------------------------
DROP TABLE IF EXISTS `sys_equipment`;
CREATE TABLE `sys_equipment`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `equipment_id` varchar(23) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备唯一编码，对同一运营商保证唯一；9组织机构+14桩编号',
  `pile_no` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '桩编号',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联充电站ID，运营商自定义的唯一编码 小于等于20字符',
  `price_code` bigint UNSIGNED NULL DEFAULT 0 COMMENT '价格模版ID',
  `manufacturer_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '设备生产商组织机构代码',
  `manufacturer_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '设备生产商名称',
  `equipment_model` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '设备型号',
  `production_date` datetime NULL DEFAULT NULL COMMENT '设备生产日期',
  `equipment_type` tinyint NOT NULL COMMENT '1直流设备 2交流设备 3交直流一体设备 4无线设备 5其他',
  `equipment_lng` decimal(10, 6) NULL DEFAULT NULL COMMENT '充电设备经度',
  `equipment_lat` decimal(10, 6) NULL DEFAULT NULL COMMENT '充电设备纬度',
  `power` decimal(10, 1) NOT NULL DEFAULT 0.0 COMMENT '充电设备总功率kW 保留小数点后1位',
  `max_power` decimal(3, 2) UNSIGNED NULL DEFAULT 1.00 COMMENT '充电桩最大允许输出功率 30%-100% 1Bin表示1%',
  `is_working` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否启用 0 正常工作 1停止使用锁定',
  `sync_tm` datetime NULL DEFAULT NULL COMMENT '最近对时时间',
  `equipment_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '充电设备名称',
  `net_type` tinyint UNSIGNED NULL DEFAULT 3 COMMENT '0、sim卡 1、LAN 2、WAN 3其他',
  `m_operator` tinyint(1) NULL DEFAULT 4 COMMENT '0移动 2电信 3联通 4其他',
  `online_tm` datetime NULL DEFAULT NULL COMMENT '最近上线时间（登录验证）',
  `serv_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '连接主机IP',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` bigint NULL DEFAULT 0 COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint NULL DEFAULT 0 COMMENT '更新人ID',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `equipment_id`(`equipment_id` ASC) USING BTREE COMMENT '充电设备编号索引',
  INDEX `sys_equipment_station_id_IDX`(`station_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '充电设备信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_operator
-- ----------------------------
DROP TABLE IF EXISTS `sys_operator`;
CREATE TABLE `sys_operator`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `operator_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运营商ID（组织机构代码）',
  `operator_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '机构全称',
  `operator_tel_1` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运营商电话1',
  `operator_tel_2` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '运营商电话2',
  `operator_reg_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '运营商注册地址',
  `operator_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '运营商国标接口http请求前缀，以/结尾',
  `operator_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '运营商密钥0-F字符组成,可采用32H、48H、64H',
  `data_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '消息密钥',
  `data_secret_iv` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '消息密钥初始化向量 固定16位 用户AES加密过程的混合加密',
  `sig_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '签名密钥0-F字符组成,可采用32H、48H、64H 为签名的加密密钥',
  `my_operator_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户平台我方组织机构编码',
  `user_operator_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户平台密钥',
  `user_data_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户平台消息密钥',
  `user_data_secret_iv` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户平台消息密钥初始化向量',
  `user_sig_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户平台签名密钥',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` bigint NULL DEFAULT 0 COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint NULL DEFAULT 0 COMMENT '更新人ID',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `operator_id`(`operator_id` ASC) USING BTREE COMMENT '运营商组织机构索引'
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础设施运营商信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_price
-- ----------------------------
DROP TABLE IF EXISTS `sys_price`;
CREATE TABLE `sys_price`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `price_code` bigint UNSIGNED NULL DEFAULT 0 COMMENT '价格模版ID，0为默认价格',
  `start_time` datetime NOT NULL COMMENT '时段起始时间点 6位 HHmmss',
  `price_type` tinyint UNSIGNED NOT NULL COMMENT '0尖1峰2平3谷',
  `elec_price` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '时段电费（小数点后4位）',
  `service_price` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '小数点后4位',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  `main_point` tinyint(1) NULL DEFAULT 0 COMMENT '主行',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_connector_price_price_code_IDX`(`price_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_station
-- ----------------------------
DROP TABLE IF EXISTS `sys_station`;
CREATE TABLE `sys_station`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电站ID，运营商自定义的唯一编码 小于等于20字符',
  `operator_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运营商ID',
  `equipment_owner_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '设备所属方ID，设备所属运营平台组织机构代码',
  `station_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电站名称',
  `country_code` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电站国家代码',
  `area_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充电站省市辖区编码 20个字符',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '详细地址',
  `station_tel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '站点电话 小于30字符',
  `service_tel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '服务电话 小于30字符',
  `station_type` int NOT NULL DEFAULT 255 COMMENT '1公共 50个人 100公交 101环卫 102物流 103出租车 255其他',
  `station_status` tinyint NOT NULL DEFAULT 0 COMMENT '0未知 1建设中 5关闭下线 6维护中 50正常使用',
  `park_nums` int NOT NULL DEFAULT 0 COMMENT '车位数量 0未知',
  `station_lng` decimal(10, 6) NOT NULL DEFAULT 0.000000 COMMENT '经度 保留小数点后6位',
  `station_lat` decimal(10, 6) NOT NULL DEFAULT 0.000000 COMMENT '纬度 保留小数点后6位',
  `site_guide` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '站点引导',
  `construction` int NULL DEFAULT 0 COMMENT '建设场所',
  `pictures` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '充电设备照片、充电车位照片、停车场入口照片，字符串数组',
  `match_cars` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '使用车型描述，描述该站点接受的车大小及类型，如大车、物流车、私家乘用车、出租车等',
  `park_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '车位楼层及数量描述，车位楼层以及数量信息',
  `business_hours` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '营业时间描述',
  `create_by_id` bigint NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by_id` bigint NULL DEFAULT 0,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` tinyint(1) NULL DEFAULT 0,
  `electricity_fee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '充电费率',
  `service_fee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '服务费率',
  `park_fee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '停车费',
  `payment` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '支付方式：刷卡、线上、现金。其中电子钱包类卡为刷卡，身份鉴权卡】微信/支付宝、APP为线上',
  `support_order` tinyint NULL DEFAULT 0 COMMENT '充电设备是否需要提前预约后才能使用。0为不支持预约；1为支持预约。默认0',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `report_gov_flag` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否需要上报市政平台（0否 其他为平台编号）',
  `report_gov` tinyint(1) NULL DEFAULT 0 COMMENT '是否已上报至政府平台',
  `is_alone_apply` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否独立报装 0否 1是；如果是独立报桩，需要填写户号以及容量',
  `account_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '户号：国网电费账单户号(小于20字符)',
  `capacity` decimal(20, 4) NOT NULL DEFAULT 0.0000 COMMENT '容量：独立电表申请的功率（保留小数点后4位）',
  `is_public_parking_lot` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是公共停车场库0否1是；如果是则需要填写场库编号',
  `parking_lot_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '公共停车场库编号(小于20字符)',
  `open_all_day` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否全天开放0否1是',
  `park_free` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否停车免费0否1是',
  `unit_flag` int NOT NULL DEFAULT 0 COMMENT '二进制，每一位代表一个信号量，从低位开始，1位卫生间，2位便利店，3位餐厅，4位休息室，5位雨棚，6位小票即，7位道闸，8位地锁',
  `min_electricity_price` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '最低充电电费率',
  `park_fee_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0停车收费1停车免费2限时免费3充电限免',
  `subsidy_per_kwh` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '每度电综合补贴单价',
  `subsidy_year_max` decimal(16, 4) NOT NULL DEFAULT 0.0000 COMMENT '站点年最大补贴金额',
  `subsidy_operator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '订单补贴，空：不补贴；1全补贴；运营商ID，补贴，多运营商逗号分隔',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `station_id`(`station_id` ASC) USING BTREE COMMENT '充电站ID索引'
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '充电站信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_station_operator_link
-- ----------------------------
DROP TABLE IF EXISTS `sys_station_operator_link`;
CREATE TABLE `sys_station_operator_link`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '站点ID',
  `operator_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运营商ID',
  `is_sync_trade` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0否 1是 是否强制同步全量订单数据，主要应用与地方补贴场景',
  `is_enable` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0不启用 1启用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` bigint NULL DEFAULT 0 COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by_id` bigint NULL DEFAULT 0 COMMENT '更新人ID',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_station_operator_link_station_id_IDX`(`station_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_station_price
-- ----------------------------
DROP TABLE IF EXISTS `sys_station_price`;
CREATE TABLE `sys_station_price`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
  `price_code` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '价格id',
  `price_type` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 默认类型 1 仅限快充 2仅限慢充 ',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `is_use` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否正在启用0 否 1是',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `create_by_id` bigint NULL DEFAULT 0 COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by_id` bigint NULL DEFAULT 0 COMMENT '更新人ID',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `station_id`(`station_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '站点-价格模型关联表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
