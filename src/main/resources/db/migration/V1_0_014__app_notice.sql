-- ----------------------------------
-- Table structure for app_notice
-- ----------------------------------
DROP TABLE IF EXISTS `app_notice`;
CREATE TABLE `app_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL COMMENT '公告内容',
  `crt_time` datetime DEFAULT NULL,
  `del_flag` int(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '系统公告表';