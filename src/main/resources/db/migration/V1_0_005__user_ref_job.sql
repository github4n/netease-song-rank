-- ----------------------------
-- Table structure for user_ref_job
-- ----------------------------
DROP TABLE IF EXISTS `user_ref_job`;
CREATE TABLE `user_ref_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_alias` varchar(100) DEFAULT NULL,
  `job_id` int(11) NOT NULL COMMENT '定时任务id',
  `openid` varchar(50) DEFAULT NULL,
  `unionid` varchar(50) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '用户任务关联表';
