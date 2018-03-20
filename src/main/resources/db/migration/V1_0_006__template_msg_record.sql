-- ----------------------------
-- Table structure for template_msg_record
-- ----------------------------
DROP TABLE IF EXISTS `template_msg_record`;
CREATE TABLE `template_msg_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(50) NOT NULL,
  `target_user_id` varchar(20) NOT NULL COMMENT '订阅用户id',
  `form_id` varchar(200) NOT NULL COMMENT '表单id',
  `template_id` varchar(100) NOT NULL COMMENT '模板id',
  `page` varchar(50) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `is_valid` int(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '模板消息推送';
