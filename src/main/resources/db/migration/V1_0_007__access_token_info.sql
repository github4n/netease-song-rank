-- ----------------------------
-- Table structure for access_token_info
-- ----------------------------
DROP TABLE IF EXISTS `access_token_info`;
CREATE TABLE `access_token_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(200) NOT NULL,
  `crt_time` datetime DEFAULT NULL,
  `invalid_time` datetime DEFAULT NULL,
  `is_valid` int(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = 'access_token';