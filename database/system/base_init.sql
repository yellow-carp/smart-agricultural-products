SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_cloud_file
-- ----------------------------
DROP TABLE IF EXISTS `base_cloud_file`;
CREATE TABLE `base_cloud_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `pid` bigint NULL DEFAULT NULL COMMENT '父级Id',
  `level` int NULL DEFAULT NULL COMMENT '树层',
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_type` int NULL DEFAULT NULL COMMENT '文件类型',
  `tag` tinyint NULL DEFAULT NULL COMMENT '标签',
  `full_path` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '全路径',
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '全名',
  `file_id` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件ID',
  `object_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'OSS对象名',
  `notes` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `owner_id` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所有者',
  `group_id` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所有组',
  `count_chi` int NULL DEFAULT 0 COMMENT '子节点数',
  `meta_info` json NULL COMMENT '扩展元数据',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '逻辑删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '云文件存储系统' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_file_info
-- ----------------------------
DROP TABLE IF EXISTS `base_file_info`;
CREATE TABLE `base_file_info`  (
  `id` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键Id',
  `file_type` tinyint NULL DEFAULT NULL COMMENT '文件类型',
  `channel` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'oss渠道',
  `size` bigint NULL DEFAULT NULL COMMENT '文件大小(字节)',
  `name` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `domain` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '域名',
  `object_name` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件存储路径',
  `digest` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件摘要',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '逻辑删除',
  `upsert_by` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作者',
  `upsert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变更时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件信息管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_site_notice
-- ----------------------------
DROP TABLE IF EXISTS `base_site_notice`;
CREATE TABLE `base_site_notice`  (
  `id` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键Id',
  `type` tinyint NULL DEFAULT NULL COMMENT '消息类型',
  `is_read` bit(1) NULL DEFAULT b'0' COMMENT '已读标记',
  `sender` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者',
  `receiver` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接受方',
  `original` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源',
  `title` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息标题',
  `body` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息体',
  `link` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息链接',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '站内通知' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;