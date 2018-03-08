-- ----------------------------
-- Table structure for user_ref_job
-- ----------------------------
DROP TABLE IF EXISTS `user_ref_job`;
CREATE TABLE `user_ref_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_alias` varchar(100) DEFAULT NULL,
  `target_userid` varchar(20) NOT NULL COMMENT '订阅用户id',
  `target_nickname` varchar(200) NOT NULL COMMENT '订阅用户昵称',
  `target_avatar` varchar(200) NOT NULL COMMENT '订阅用户头像',
  `openid` varchar(50) NOT NULL,
  `unionid` varchar(50) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  UNIQUE KEY `target_userid_openid` (`target_userid`,`openid`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '用户任务关联表';
