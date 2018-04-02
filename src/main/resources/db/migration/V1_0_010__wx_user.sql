-- ----------------------------------
-- Table structure for wx_user
-- ----------------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickName` varchar(64) NOT NULL COMMENT '昵称',
  `gender` int(1) DEFAULT 1 COMMENT '性别',
  `avatarUrl` varchar(255) DEFAULT NULL COMMENT '头像',
  `country` varchar(100) DEFAULT NULL COMMENT '国家',
  `province` varchar(100) DEFAULT NULL COMMENT '省份',
  `city` varchar(100) DEFAULT NULL COMMENT '城市',
  `language` varchar(20) DEFAULT NULL COMMENT '语言',
  `openid` varchar(50) NOT NULL,
  `limit` int(3) DEFAULT 3 COMMENT '关注上限数量',
  `crt_time` datetime DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '用户信息表';