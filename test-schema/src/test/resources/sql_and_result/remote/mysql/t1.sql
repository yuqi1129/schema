/*
 Navicat Premium Data Transfer

 Source Server         : Mac连接
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : yuqi

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 19/02/2020 11:35:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t1
-- ----------------------------
DROP TABLE IF EXISTS `t1`;
CREATE TABLE `t1` (
  `tiny_type` tinyint(4) DEFAULT NULL,
  `smallint_type` smallint(6) DEFAULT NULL,
  `int_type` int(11) DEFAULT NULL,
  `bigint_type` bigint(20) DEFAULT NULL,
  `varchar_type` varchar(20) DEFAULT NULL,
  `float_type` float DEFAULT NULL,
  `double_type` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t1
-- ----------------------------
BEGIN;
INSERT INTO `t1` VALUES (1, 1, 2, 30, 'vn', 0.5, 4.8);
INSERT INTO `t1` VALUES (NULL, 10, 6, 18, 'net', 1.3, 0.6);
INSERT INTO `t1` VALUES (1, NULL, 2, 3, 'zh', 0.01, 0.6);
INSERT INTO `t1` VALUES (5, 3, NULL, 21, 'cn', 1, 0);
INSERT INTO `t1` VALUES (3, 1, 22, NULL, 'kr', 1.5, 4.3);
INSERT INTO `t1` VALUES (2, 8, 6, 1, 'us', 2.2, 0.6);
INSERT INTO `t1` VALUES (10, 1, 2, 3, NULL, 1.4, 0.6);
INSERT INTO `t1` VALUES (1, 2, 7, 11, 'uk', NULL, 1.8);
INSERT INTO `t1` VALUES (0, 1, -1, 2, 'fr', 5, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
