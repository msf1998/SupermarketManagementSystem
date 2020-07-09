/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50730
Source Host           : localhost:3306
Source Database       : sms

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-07-09 17:38:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_log`;
CREATE TABLE `tb_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `describe` varchar(1024) NOT NULL,
  `creator` char(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_log
-- ----------------------------

-- ----------------------------
-- Table structure for tb_number
-- ----------------------------
DROP TABLE IF EXISTS `tb_number`;
CREATE TABLE `tb_number` (
  `id` char(11) NOT NULL,
  `name` char(12) NOT NULL,
  `phone` char(11) NOT NULL,
  `id_number` char(18) NOT NULL,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `score` double NOT NULL DEFAULT '0',
  `parent` char(11) NOT NULL,
  `deleted` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_number
-- ----------------------------
INSERT INTO `tb_number` VALUES ('1', '普通用户', '17852032649', '371323199806212819', '2020-06-20 08:50:25', '0', '1', '0');
INSERT INTO `tb_number` VALUES ('17852032649', '郭明辉', '17852032315', '371323199806215849', '2020-07-08 16:28:23', '0', '17852032649', '0');

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creator` char(11) NOT NULL,
  `parent` char(11) NOT NULL,
  `describe` varchar(255) DEFAULT NULL,
  `sum` decimal(10,0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES ('2', '2020-07-08 15:55:20', '17852032649', '13616493716', null, '2648');
INSERT INTO `tb_order` VALUES ('3', '2020-07-03 09:30:39', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('4', '2020-07-03 15:48:42', '17852032649', '13616493716', 'hello world', '0');
INSERT INTO `tb_order` VALUES ('5', '2020-07-03 09:30:40', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('6', '2020-07-03 09:30:40', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('7', '2020-07-03 09:30:40', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('8', '2020-07-03 09:30:41', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('9', '2020-07-03 09:30:41', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('12', '2020-07-03 09:30:41', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('13', '2020-07-03 09:30:42', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('14', '2020-07-03 09:30:42', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('15', '2020-07-03 09:30:42', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('16', '2020-07-03 09:30:42', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('17', '2020-07-03 09:30:43', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('18', '2020-07-03 09:30:43', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('19', '2020-07-03 09:30:43', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('20', '2020-07-03 09:30:43', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('21', '2020-07-03 09:30:44', '17852032649', '13616493716', null, '0');
INSERT INTO `tb_order` VALUES ('23', '2020-07-08 16:06:24', '17852032649', '17852032649', null, '1324');
INSERT INTO `tb_order` VALUES ('24', '2020-07-08 16:28:23', '17852032649', '17852032649', null, '1324');

-- ----------------------------
-- Table structure for tb_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_detail`;
CREATE TABLE `tb_order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product` varchar(32) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_price` decimal(10,2) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '1',
  `order_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_order_detail
-- ----------------------------
INSERT INTO `tb_order_detail` VALUES ('1', '2341593586651401', '3142', '1324.00', '2', '2');
INSERT INTO `tb_order_detail` VALUES ('2', '2341593586651401', '3124', '1324.00', '1', '23');
INSERT INTO `tb_order_detail` VALUES ('3', '2341593586651401', '3124', '1324.00', '1', '24');

-- ----------------------------
-- Table structure for tb_product
-- ----------------------------
DROP TABLE IF EXISTS `tb_product`;
CREATE TABLE `tb_product` (
  `id` char(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `photo` varchar(255) NOT NULL,
  `manufacturer` varchar(255) NOT NULL,
  `product_date` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `self_life` int(11) NOT NULL,
  `warn_before` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  `warn_count` int(11) NOT NULL,
  `in_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `in_price` decimal(10,2) NOT NULL,
  `out_price` decimal(10,2) NOT NULL,
  `parent` char(11) NOT NULL,
  `deleted` int(11) NOT NULL DEFAULT '0',
  `type` int(11) NOT NULL,
  `q_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_product
-- ----------------------------
INSERT INTO `tb_product` VALUES ('12341593656132004', '茅台', 'default.jpg', '654撒旦', '2020-07-06 09:21:51', '54', '82', '212', '500', '2020-07-06 09:21:51', '12.00', '19.00', '17852032649', '0', '2', 'b61037f2-7906-4245-a044-f654cf104716.png');
INSERT INTO `tb_product` VALUES ('202007071594105480436', '牛栏山二锅头', 'default.jpg', '牛栏山', '2020-07-06 00:00:00', '65', '456', '500', '100', '2020-07-07 15:04:40', '564.00', '465.00', '17852032649', '0', '4', '97b6dc54-5f82-4940-b267-bc34e8424ffe.png');
INSERT INTO `tb_product` VALUES ('2341593586651401', '3124', 'c2961116-0323-4162-8fb0-08b190c387ca1594275985033jpg', '3124', '2020-07-09 14:26:25', '122', '1234', '1322', '1234', '2020-07-09 14:26:25', '1232.00', '1324.00', '17852032649', '0', '1', 'd0371ed2-ae29-4433-94b9-08bfb6e84059.png');
INSERT INTO `tb_product` VALUES ('4651592898324201', '456', 'default.jpg', '564', '2020-07-06 20:28:38', '65', '456', '10', '100', '2020-07-06 20:28:38', '564.00', '465.00', '17852032649', '0', '3', '');
INSERT INTO `tb_product` VALUES ('4651592898327985', '蛋黄派', 'default.jpg', '巴啦啦能量有限公司', '2019-06-25 15:00:35', '3', '30', '200', '40', '2020-07-06 20:31:57', '6.00', '7.00', '17852032649', '0', '4', '');
INSERT INTO `tb_product` VALUES ('4651592898329816', '456', 'default.jpg', '564', '2020-07-01 16:45:27', '65', '456', '564', '456', '2020-07-01 16:45:27', '564.00', '465.00', '17852032649', '0', '19', '');

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(32) NOT NULL,
  `product_insert` int(11) NOT NULL DEFAULT '0',
  `product_delete` int(11) NOT NULL DEFAULT '0',
  `product_update` int(11) NOT NULL DEFAULT '0',
  `product_select` int(11) NOT NULL DEFAULT '0',
  `number_insert` int(11) NOT NULL DEFAULT '0',
  `number_delete` int(11) NOT NULL DEFAULT '0',
  `number_update` int(11) NOT NULL DEFAULT '0',
  `number_select` int(11) NOT NULL DEFAULT '0',
  `type_insert` int(11) NOT NULL DEFAULT '0',
  `type_delete` int(11) NOT NULL DEFAULT '0',
  `type_update` int(11) NOT NULL DEFAULT '0',
  `type_select` int(11) NOT NULL DEFAULT '0',
  `order_insert` int(11) NOT NULL DEFAULT '0',
  `order_delete` int(11) NOT NULL DEFAULT '0',
  `order_update` int(11) NOT NULL DEFAULT '0',
  `order_select` int(11) NOT NULL DEFAULT '0',
  `user_insert` int(11) NOT NULL DEFAULT '0',
  `user_delete` int(11) NOT NULL DEFAULT '0',
  `user_update` int(11) NOT NULL DEFAULT '0',
  `user_select` int(11) NOT NULL DEFAULT '0',
  `role_insert` int(11) NOT NULL DEFAULT '0',
  `role_delete` int(11) NOT NULL DEFAULT '0',
  `role_update` int(11) NOT NULL DEFAULT '0',
  `role_select` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `parent` char(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES ('1', '超级管理员', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2020-07-01 20:19:28', '1');
INSERT INTO `tb_role` VALUES ('2', '售货员', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2020-07-01 20:19:32', '1');
INSERT INTO `tb_role` VALUES ('26', '仓库管理员', '1', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2020-07-09 14:29:33', '17852032649');
INSERT INTO `tb_role` VALUES ('27', '经理', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', '0', '0', '1', '2020-07-05 16:39:42', '17852032649');

-- ----------------------------
-- Table structure for tb_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_type`;
CREATE TABLE `tb_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `parent` char(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_type
-- ----------------------------
INSERT INTO `tb_type` VALUES ('1', '赠品', '2020-07-06 14:12:08', '17852032649');
INSERT INTO `tb_type` VALUES ('2', '促销', '2020-07-06 14:12:22', '17852032649');
INSERT INTO `tb_type` VALUES ('3', '积压', '2020-07-06 14:12:30', '17852032649');
INSERT INTO `tb_type` VALUES ('4', '酒水', '2020-07-06 14:12:49', '17852032649');
INSERT INTO `tb_type` VALUES ('19', '厨卫用品', '2020-07-06 14:11:24', '17852032649');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` char(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` char(4) NOT NULL,
  `head` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(32) NOT NULL,
  `role` int(11) NOT NULL DEFAULT '2',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('0', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-04 21:44:11', '1');
INSERT INTO `tb_user` VALUES ('1', 'jkhljkhihlvycrcvjldoifew9riuq39rjewfndkjn,.', '7j*)', 'default.jpg', 'root', '1', '2020-06-20 08:49:23', '0');
INSERT INTO `tb_user` VALUES ('13616493716', 'Dqa6A+Q2N/AEEZyDcuCD7w==', '384', '136164937161594265635464.jpg', '马经理', '27', '2020-07-09 14:18:41', '0');
INSERT INTO `tb_user` VALUES ('17852030453', 'CTuFHFA3fjagckeNoMNcLQ==', '346', '178520304531594286378746.jpg', '郭明辉', '2', '2020-07-09 17:25:13', '0');
INSERT INTO `tb_user` VALUES ('17852030754', 'SJp8SQWix8Mfq0Ux5vlUig==', '220', '178520320751594271298511.jpg', '常超', '2', '2020-07-09 14:07:52', '0');
INSERT INTO `tb_user` VALUES ('17852032122', 'MzbRzkFgsrxP8o951JGeLQ==', '375', 'default.jpg', '单俊凯', '26', '2020-07-09 14:16:34', '0');
INSERT INTO `tb_user` VALUES ('17852032649', 'MzbRzkFgsrxP8o951JGeLQ==', '375', '178520326491594270153034.jpg', '马丰顺', '1', '2020-07-09 17:24:41', '0');
INSERT INTO `tb_user` VALUES ('2', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-04 21:44:07', '1');
INSERT INTO `tb_user` VALUES ('3', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-08 22:57:45', '1');
INSERT INTO `tb_user` VALUES ('4', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-08 22:57:42', '1');
INSERT INTO `tb_user` VALUES ('5', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-08 22:57:48', '1');
INSERT INTO `tb_user` VALUES ('6', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-09 13:08:35', '1');
INSERT INTO `tb_user` VALUES ('7', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-09 13:08:38', '1');
INSERT INTO `tb_user` VALUES ('8', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-09 13:11:08', '1');
INSERT INTO `tb_user` VALUES ('9', 'ads', 'asd', 'asd', 'sd', '2', '2020-07-09 13:11:11', '1');
SET FOREIGN_KEY_CHECKS=1;
