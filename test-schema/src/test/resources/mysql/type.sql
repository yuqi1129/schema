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

 Date: 17/02/2020 10:58:47
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
INSERT INTO `type` VALUES (1, 1, 2, 30, 'zhang', 'vn', 0.5, 4.8);
INSERT INTO `type` VALUES (NULL, 10, 6, 18, 'wang', 'net', 1.3, 0.6);
INSERT INTO `type` VALUES (1, NULL, 2, 3, 'cheng', 'zh', 0.01, 0.6);
INSERT INTO `type` VALUES (5, 3, NULL, 21, 'yu', 'cn', 1, 0);
INSERT INTO `type` VALUES (3, 1, 22, NULL, 'li', 'kr', 1.5, 4.3);
INSERT INTO `type` VALUES (2, 8, 6, 1, NULL, 'us', 2.2, 0.6);
INSERT INTO `type` VALUES (10, 1, 2, 3, 'zhao', NULL, 1.4, 0.6);
INSERT INTO `type` VALUES (1, 2, 7, 11, 'sun', 'uk', NULL, 1.8);
INSERT INTO `type` VALUES (0, 1, -1, 2, 's', 'fr', 5, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
