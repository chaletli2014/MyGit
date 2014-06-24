/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50523
Source Host           : localhost:3306
Source Database       : lskpi

Target Server Type    : MYSQL
Target Server Version : 50523
File Encoding         : 65001

Date: 2014-01-09 16:38:06
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `tbl_month_data`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_month_data`;
CREATE TABLE `tbl_month_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pedEmernum` int(11) DEFAULT NULL,
  `pedroomnum` int(11) DEFAULT NULL,
  `resnum` int(11) DEFAULT NULL,
  `other` int(11) DEFAULT NULL,
  `operatorName` varchar(20) DEFAULT NULL,
  `operatorCode` varchar(20) DEFAULT NULL,
  `hospitalCode` varchar(20) DEFAULT NULL,
  `dsmCode` varchar(20) DEFAULT NULL,
  `rsmRegion` varchar(20) DEFAULT NULL,
  `region` varchar(20) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `updatedate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_month_data
-- ----------------------------
INSERT INTO `tbl_month_data` VALUES ('1', '100', '20', '10', '11', '张宇', '7019563', 'SHSH108H', '7008485', 'SH RE', 'East1 GRA', '2013-12-24 16:44:14', '2013-12-24 16:44:14');
INSERT INTO `tbl_month_data` VALUES ('2', '500', '20', '77', '222', '张宇', '8249167', 'SHSH152H', '7008485', 'SH RE', 'East1 GRA', '2013-12-24 16:46:56', '2013-12-24 16:46:56');
INSERT INTO `tbl_month_data` VALUES ('3', '1000', '1000', '150', '100', '胡斌', '7008485', 'SHSH084H', '7008485', 'SH RE', 'East1 GRA', '2013-11-25 15:30:29', '2013-12-25 15:30:29');
INSERT INTO `tbl_month_data` VALUES ('4', '100', '200', '300', '100', '毛茜', '8096056', 'SCCD001H', '7015926', 'SC&CQ RE', 'West GRA', '2013-12-08 16:18:34', '2014-01-08 16:18:34');
INSERT INTO `tbl_month_data` VALUES ('5', '100', '100', '100', '100', '钱军', '8231946', 'ZJHZ023H', '7010308', 'ZJ RE1', 'East2 GRA', '2013-12-08 16:18:34', '2014-01-08 16:19:15');
INSERT INTO `tbl_month_data` VALUES ('6', '100', '100', '100', '20', '刘叶', '7022917', 'ZJWZ016H', '7005195', 'ZJ RE2', 'East2 GRA', '2013-12-08 16:18:34', '2014-01-08 16:19:42');
INSERT INTO `tbl_month_data` VALUES ('7', '300', '200', '100', '10', '马向梅', '8091031', 'YNKM031H', '7012181', 'YN GRA', 'South GRA', '2013-12-08 16:18:34', '2014-01-08 16:20:21');
INSERT INTO `tbl_month_data` VALUES ('8', '200', '200', '200', '210', '马向梅', '8091031', 'YNKM009H', '7012181', 'YN GRA', 'South GRA', '2013-12-08 16:18:34', '2014-01-08 16:20:32');
INSERT INTO `tbl_month_data` VALUES ('9', '20', '100', '20', '10', '马向梅', '8091031', 'YN00406N', '7012181', 'YN GRA', 'South GRA', '2013-12-08 16:18:34', '2014-01-08 16:20:40');
INSERT INTO `tbl_month_data` VALUES ('10', '100', '120', '100', '80', '华君', '8124524', 'SHSH080H', '7008485', 'SH RE', 'East1 GRA', '2013-12-08 16:18:34', '2014-01-08 16:21:06');
INSERT INTO `tbl_month_data` VALUES ('11', '100', '200', '100', '30', '刘佳', '8266791', 'SC02658N', '7022977', 'SC&CQ RE', 'West GRA', '2013-12-08 16:18:34', '2014-01-08 16:21:34');
INSERT INTO `tbl_month_data` VALUES ('12', '200', '100', '100', '100', '李浚冬', '7022737', 'YN00643N', '5201813', 'YN GRA', 'South GRA', '2013-12-09 14:02:19', '2014-01-09 14:02:19');
INSERT INTO `tbl_month_data` VALUES ('13', '100', '20', '20', '10', '李浚冬', '7022737', 'YN05679N', '5201813', 'YN GRA', 'South GRA', '2013-12-09 14:02:29', '2014-01-09 14:02:29');

INSERT INTO `tbl_month_data` VALUES ('14', '80', '120', '400', '200', '毛茜', '8096056', 'SCCD001H', '7015926', 'SC&CQ RE', 'West GRA', '2013-11-08 16:18:34', '2014-01-08 16:18:34');
INSERT INTO `tbl_month_data` VALUES ('15', '70', '50', '80', '80', '钱军', '8231946', 'ZJHZ023H', '7010308', 'ZJ RE1', 'East2 GRA', '2013-11-08 16:18:34', '2014-01-08 16:19:15');
INSERT INTO `tbl_month_data` VALUES ('16', '120', '110', '90', '20', '刘叶', '7022917', 'ZJWZ016H', '7005195', 'ZJ RE2', 'East2 GRA', '2013-11-08 16:18:34', '2014-01-08 16:19:42');
INSERT INTO `tbl_month_data` VALUES ('17', '280', '210', '80', '50', '马向梅', '8091031', 'YNKM031H', '7012181', 'YN GRA', 'South GRA', '2013-11-08 16:18:34', '2014-01-08 16:20:21');
INSERT INTO `tbl_month_data` VALUES ('18', '120', '210', '120', '190', '马向梅', '8091031', 'YNKM009H', '7012181', 'YN GRA', 'South GRA', '2013-11-08 16:18:34', '2014-01-08 16:20:32');
INSERT INTO `tbl_month_data` VALUES ('19', '30', '80', '40', '30', '马向梅', '8091031', 'YN00406N', '7012181', 'YN GRA', 'South GRA', '2013-11-08 16:18:34', '2014-01-08 16:20:40');
INSERT INTO `tbl_month_data` VALUES ('20', '110', '110', '110', '100', '华君', '8124524', 'SHSH080H', '7008485', 'SH RE', 'East1 GRA', '2013-11-08 16:18:34', '2014-01-08 16:21:06');
INSERT INTO `tbl_month_data` VALUES ('21', '110', '120', '110', '20', '刘佳', '8266791', 'SC02658N', '7022977', 'SC&CQ RE', 'West GRA', '2013-11-08 16:18:34', '2014-01-08 16:21:34');
INSERT INTO `tbl_month_data` VALUES ('22', '190', '120', '110', '80', '李浚冬', '7022737', 'YN00643N', '5201813', 'YN GRA', 'South GRA', '2013-11-09 14:02:19', '2014-01-09 14:02:19');
INSERT INTO `tbl_month_data` VALUES ('23', '120', '50', '40', '60', '李浚冬', '7022737', 'YN05679N', '5201813', 'YN GRA', 'South GRA', '2013-11-09 14:02:29', '2014-01-09 14:02:29');
