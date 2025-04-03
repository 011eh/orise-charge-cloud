/*
 Navicat Premium Data Transfer

 Source Server         : omind
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : omind-user-plat

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 03/04/2025 16:40:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for omind_app
-- ----------------------------
DROP TABLE IF EXISTS `omind_app`;
CREATE TABLE `omind_app`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `app_type` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 openAPI 1 奥升小程序',
  `app_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '应用名',
  `app_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '应用key',
  `secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '应用密钥',
  `valid_time` bigint UNSIGNED NOT NULL DEFAULT 253402271999 COMMENT '有效期',
  `state` tinyint NOT NULL DEFAULT 0 COMMENT '启用标记0未启用 1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_bill
-- ----------------------------
DROP TABLE IF EXISTS `omind_bill`;
CREATE TABLE `omind_bill`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
  `base_operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础平台运营商id(组织机构代码)',
  `start_charge_seq` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '格式\"运营商ID+唯一编号\",27字符',
  `connector_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电设备接口编码(充电设备接口编码，同一运营商内唯一)',
  `start_charge_seq_stat` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '充电订单状态:1、启动中;2、充电中;3、停止中;4、已结束;5、未知;8、异常订单;20、已处理异常订单',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '充电者用户id',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始充电时间(格式\"yyyy-MM-dd HH:mm:ss\")',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束充电时间(格式\"yyyy-MM-dd HH:mm:ss\")',
  `total_power` decimal(6, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '累计充电量(单位:度,小数点后2位)',
  `total_elec_money` decimal(6, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '总电费(单位:元,小数点后2位)',
  `total_service_money` decimal(6, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '总服务费(单位:元,小数点后2位)',
  `total_money` decimal(6, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '累计总金额(单位:元,小数点后2位)',
  `real_pay_money` decimal(6, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '实际支付总金额(单位:元,小数点后2位)',
  `stop_reason` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '充电结束原因:0、用户手动停止充电;1、客户归属地运营商平台停止充电;2、BMS停止充电;3、充电机设备故障;4、连接器断开;5-99、自定义',
  `stop_fail_reason` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '停止失败原因:0、无;1、此设备不存在;2、此设备离线;3、设备已停止充电;4~99、自定义',
  `sum_period` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '时段数N，范围：0～32',
  `charge_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '交易信息 json',
  `car_vin` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车辆识别码',
  `plate_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车牌号',
  `succ_stat` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '成功标识:0、成功;1、失败;',
  `bill_type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '订单类型 0扫码充电 1刷卡充电',
  `soc` decimal(6, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '电池剩余电量(默认:0)',
  `pay_state` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否支付:0、未支付;1、已支付;',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `price_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '价格信息 json',
  `pay_plat` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电平台',
  `transaction_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '支付交易号(如微信)',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  `currentA` decimal(6, 2) NOT NULL DEFAULT 0.00 COMMENT 'A相电流(单位:A,默认:0 含直流(输出))',
  `voltageA` decimal(6, 2) NULL DEFAULT 0.00 COMMENT 'A相电压(单位:V,默认:0 含直流(输出))',
  `charge_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '充电类型 1、充满;2、按金额充电',
  `charge_money` decimal(6, 2) NOT NULL DEFAULT 0.00 COMMENT '用户预计充电金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `start_charge_seq`(`start_charge_seq` ASC) USING BTREE,
  INDEX `connector_id`(`connector_id` ASC) USING BTREE,
  INDEX `create_time`(`create_time` ASC) USING BTREE,
  INDEX `station_id`(`station_id` ASC) USING BTREE,
  INDEX `start_time`(`start_time` ASC) USING BTREE,
  INDEX `end_time`(`end_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11485 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '充电订单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_connector
-- ----------------------------
DROP TABLE IF EXISTS `omind_connector`;
CREATE TABLE `omind_connector`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
  `operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础运营商id(组织机构代码)',
  `user_operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户平台运营商id(组织机构代码)',
  `base_operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础运营商id(组织机构代码)',
  `equipment_id` varchar(23) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备编码(设备唯一编码，对同一运营商，保证唯一)',
  `connector_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电设备接口编码(充电设备接口编码，同一运营商内唯一)',
  `connector_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电设备接口名称',
  `connector_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '充电设备接口类型:1、家用插座(模式2);2、交流接口插座(模式3，连接方式B);3、交流接口插头(带枪线，模式3，连接方式C);4、直流接口枪头(带枪线，模式4);5、无线充电座;6、其他',
  `voltage_upper_limits` smallint UNSIGNED NOT NULL DEFAULT 0 COMMENT '额定电压上限(单位:V)',
  `voltage_lower_limits` smallint UNSIGNED NOT NULL DEFAULT 0 COMMENT '额定电压下限(单位:V)',
  `current_value` smallint UNSIGNED NOT NULL COMMENT '额定电流(单位:A)',
  `power` decimal(6, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '额定功率(单位:kW)',
  `park_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车位号(停车场车位编号)',
  `national_standard` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '国家标准:1、2011;2、2015',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '充电设备接口状态:0、离网;1、空闲;2、占用(未充电);3、占用(充电中);4、占用(预约锁定);255、故障',
  `park_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '车位状态:0:未知;10:空闲;50:已上锁',
  `lock_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '地锁状态:0:未知;10:已解锁;50:已上锁',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `equipment_id`(`equipment_id` ASC) USING BTREE,
  INDEX `connector_id`(`connector_id` ASC) USING BTREE,
  INDEX `station_id`(`station_id` ASC) USING BTREE,
  INDEX `operator_id`(`operator_id` ASC) USING BTREE,
  INDEX `base_operator_id`(`base_operator_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4534 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '充电设备接口信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_equipment
-- ----------------------------
DROP TABLE IF EXISTS `omind_equipment`;
CREATE TABLE `omind_equipment`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
  `operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营商id(组织机构代码)',
  `user_operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户平台运营商id(组织机构代码)',
  `base_operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础运营商id(组织机构代码)',
  `equipment_id` varchar(23) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备编码(设备唯一编码，对同一运营商，保证唯一)',
  `equipment_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电设备名称',
  `manufacturer_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备生产商组织机构代码',
  `manufacturer_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备生产商名称',
  `equipment_model` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备型号(由设备生产商定义的设备型号)',
  `production_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备生产日期(YYYY-MM-DD)',
  `equipment_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '设备类型:1、直流设备;2、交流设备;3、交直流一体设备;4、无线设备;5、其他',
  `equipment_lng` decimal(6, 6) UNSIGNED NOT NULL DEFAULT 0.000000 COMMENT '站点经度(GCJ-02坐标系,保留小数点后6位)',
  `equipment_lat` decimal(6, 6) UNSIGNED NOT NULL DEFAULT 0.000000 COMMENT '站点纬度(GCJ-02坐标系,保留小数点后6位)',
  `power` decimal(6, 1) UNSIGNED NOT NULL DEFAULT 0.0 COMMENT '充电设备总功率(单位:kW,保留小数点后1位)',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `operator_id`(`operator_id` ASC) USING BTREE,
  INDEX `station_id`(`station_id` ASC) USING BTREE,
  INDEX `equipment_id`(`equipment_id` ASC) USING BTREE,
  INDEX `manufacturer_id`(`manufacturer_id` ASC) USING BTREE,
  INDEX `base_operator_id`(`base_operator_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3572 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '充电设备信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_feedback
-- ----------------------------
DROP TABLE IF EXISTS `omind_feedback`;
CREATE TABLE `omind_feedback`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `connector_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '桩ID',
  `feedback_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '反馈类型:1、充电站;2、充电桩;3、充电枪;50、其他',
  `feedback_content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '反馈内容',
  `imgs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片',
  `reply_content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '回复内容',
  `reply_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否回复:0、未回复;1、已回复',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_operator
-- ----------------------------
DROP TABLE IF EXISTS `omind_operator`;
CREATE TABLE `omind_operator`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营商id(组织机构代码)',
  `operator_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营商名称',
  `operator_tel1` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营商客服电话1',
  `operator_tel2` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营商客服电话2',
  `operator_reg_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营商注册地址',
  `operator_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注信息',
  `user_operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '客户归属运营商id(组织机构代码)',
  `operator_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营商密钥',
  `data_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消息密钥',
  `data_secret_iv` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消息密钥初始化向量',
  `sig_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '签名密钥',
  `base_operator_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础运营商密钥',
  `base_data_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础消息密钥',
  `base_data_secret_iv` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础消息密钥初始化向量',
  `base_sig_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础签名密钥',
  `api_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '接口地址',
  `plat_type` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '平台类型:1、OMIND',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `operator_id`(`operator_id` ASC) USING BTREE,
  INDEX `user_operator_id`(`user_operator_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '基础设施运营商信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_price
-- ----------------------------
DROP TABLE IF EXISTS `omind_price`;
CREATE TABLE `omind_price`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站ID',
  `price_code` bigint NOT NULL DEFAULT 0 COMMENT '价格模版ID，0为默认价格',
  `start_time` datetime NOT NULL COMMENT '时段起始时间点 6位 HHmmss',
  `price_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '价格类型:0、尖;1、峰;2、平;3、谷;',
  `elec_price` decimal(10, 4) UNSIGNED NOT NULL DEFAULT 0.0000 COMMENT '电价:XXXX.XXXX',
  `service_price` decimal(10, 4) UNSIGNED NOT NULL DEFAULT 0.0000 COMMENT '服务费单价:XXXX.XXXX',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '充电价格表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_station
-- ----------------------------
DROP TABLE IF EXISTS `omind_station`;
CREATE TABLE `omind_station`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
  `operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '运营商id(组织机构代码)',
  `user_operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户平台运营商id(组织机构代码)',
  `base_operator_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础运营商id(组织机构代码)',
  `equipment_owner_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备所属商id(组织机构代码)',
  `station_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站名称',
  `country_code` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站国家代码,比如CN',
  `area_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站省市辖区编码',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细地址',
  `station_tel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '站点电话(能够联系场站工作人员进行协助的联系电话)',
  `service_tel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '服务电话(平台服务电话，例如400的电话)',
  `station_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '站点类型:1、公共;50、个人;100、公交(专用);101、环卫(专用);102、物流(专用);103、出租车(专用);255、其他',
  `station_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '站点状态:0、未知;1、建设中;5、关闭下线;6、维护中;50、正常使用',
  `park_nums` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '车位数量(可停放进行充电的车位总数，默认：0 未知)',
  `station_lng` decimal(10, 6) UNSIGNED NOT NULL DEFAULT 0.000000 COMMENT '站点经度(GCJ-02坐标系,保留小数点后6位)',
  `station_lat` decimal(10, 6) UNSIGNED NOT NULL DEFAULT 0.000000 COMMENT '站点纬度(GCJ-02坐标系,保留小数点后6位)',
  `site_guide` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '站点引导:描述性文字，用于引导车主找到充电车位',
  `construction` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '建设场所类型:1、居民区;2、公共机构;3、企事业单位;4、写字楼;5、工业园区;6、交通枢纽;7、大型文体设施;8、城市绿地;9、大型建筑配建停车场;10、路边停车位;11、城际高速服务区;255、其他',
  `pictures` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '站点照片(充电设备照片、充电车位照片、停车场入口照片)JSON串',
  `match_cars` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '使用车型描述(描述该站点接受的车大小以及类型，如大巴、物流车、私家乘用车、出租车等)',
  `park_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车位楼层及数量描述(车位楼层以及数量信息)',
  `busine_hours` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '营业时间描述',
  `electricity_fee` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电电费描述',
  `service_fee` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '服务费描述',
  `park_fee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '停车费描述',
  `payment` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '支付方式::刷卡、线上、现金。其中电子钱包类卡为刷卡，身份鉴权卡、微信/支付宝、APP为线上',
  `support_order` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否支持预约(充电设备是否需要提前预约后才能使用。0为不支持预约;1为支持预约。不填默认为0)',
  `plat_type` tinyint UNSIGNED NOT NULL COMMENT '平台类型',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注信息',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `station_id`(`station_id` ASC) USING BTREE,
  INDEX `operator_id`(`operator_id` ASC) USING BTREE,
  INDEX `equipment_owner_id`(`equipment_owner_id` ASC) USING BTREE,
  INDEX `area_code`(`area_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '充电站信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_station_images
-- ----------------------------
DROP TABLE IF EXISTS `omind_station_images`;
CREATE TABLE `omind_station_images`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `station_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
  `image_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '图片类型:1、充电站照片;2、充电桩照片;3、充电车位照片;4、停车场入口照片',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '站点图片',
  `image_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '图片名称',
  `show_seq` int UNSIGNED NULL DEFAULT 0 COMMENT '显示顺序',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '充电站图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_user
-- ----------------------------
DROP TABLE IF EXISTS `omind_user`;
CREATE TABLE `omind_user`  (
  `uid` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `phone_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '国家区号码',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户手机号',
  `nick_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
  `wechat_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '微信昵称',
  `unionid_wx` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当且仅当该移动应用已获得该用户的 userinfo 授权时，才会出现该字段',
  `openid_wx` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '小程序openID',
  `unionid_ali` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'ali统一id',
  `openid_ali` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '支付宝用户唯一标识',
  `credit_pay_wx` int NOT NULL DEFAULT 0 COMMENT '微信信用分授权-后支付',
  `credit_pay_ali` int NOT NULL DEFAULT 0 COMMENT '阿里信用分授权-后支付',
  `sex` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0、未知;1、男;2、女',
  `country` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '国家',
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '省',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '城市',
  `birthday` bigint NOT NULL DEFAULT 0 COMMENT '生日',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '头像地址',
  `platform` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '注册来源的平台:关联平台id(0未知)',
  `disable_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否禁用用户:0、启用;1、禁用',
  `register_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `last_visit_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次访问时间',
  `last_visit_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '最近一次访问ip',
  `last_visit_area` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '最近一次访问ip对应区域',
  `org_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户是否从属组织机构:0、不从属;1、从属',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `mobile`(`mobile` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for omind_user_car
-- ----------------------------
DROP TABLE IF EXISTS `omind_user_car`;
CREATE TABLE `omind_user_car`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `plate_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车牌号',
  `car_vin` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车辆vin码',
  `engine_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '发动机号码',
  `vehicle_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车辆类型',
  `car_model` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '品牌型号',
  `owner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车辆所有人',
  `address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '住址',
  `use_character` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '使用性质:运营、非运营',
  `register_date` date NULL DEFAULT NULL COMMENT '注册日期(格式\"yyyy-MM-dd\")',
  `issue_date` date NULL DEFAULT NULL COMMENT '发证日期(格式\"yyyy-MM-dd\")',
  `license_imgs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '行驶证图片json串',
  `check_state` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核状态:0、待审核;1、审核通过;2、审核不通过;3、不需审核',
  `auth_state` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '认证状态:0、不认证;1、待认证;2、认证通过;3、认证不通过',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
  `del_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据状态:0、正常;1、删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '000000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `plate_no`(`plate_no` ASC) USING BTREE,
  INDEX `car_vin`(`car_vin` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户的车辆' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
