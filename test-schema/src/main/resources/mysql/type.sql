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

 Date: 13/02/2020 17:57:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type` (
  `tiny_type` tinyint(4) DEFAULT NULL,
  `smallint_type` smallint(6) DEFAULT NULL,
  `int_type` int(11) DEFAULT NULL,
  `bigint_type` bigint(20) DEFAULT NULL,
  `char_type` char(10) DEFAULT NULL,
  `varchar_type` varchar(20) DEFAULT NULL,
  `float_type` float DEFAULT NULL,
  `double_type` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of type
-- ----------------------------
BEGIN;
INSERT INTO `type` VALUES (1, 1, 2, 3, 'zhang', 'lisi', 0.5, 0.6);
INSERT INTO `type` VALUES (NULL, 1, 2, 3, 'zhang', 'lisi', 0.5, 0.6);
INSERT INTO `type` VALUES (1, NULL, 2, 3, 'zhang', 'lisi', 0.5, 0.6);
INSERT INTO `type` VALUES (1, 1, NULL, 3, 'zhang', 'lisi', 0.5, 0.6);
INSERT INTO `type` VALUES (1, 1, 2, NULL, 'zhang', 'lisi', 0.5, 0.6);
INSERT INTO `type` VALUES (1, 1, 2, 3, NULL, 'lisi', 0.5, 0.6);
INSERT INTO `type` VALUES (1, 1, 2, 3, 'zhang', NULL, 0.5, 0.6);
INSERT INTO `type` VALUES (1, 1, 2, 3, 'zhang', 'lisi', NULL, 0.6);
INSERT INTO `type` VALUES (1, 1, 2, 3, 'zhang', 'lisi', 0.5, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
