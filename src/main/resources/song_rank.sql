SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for access_token_info
-- ----------------------------
DROP TABLE IF EXISTS `access_token_info`;
CREATE TABLE `access_token_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(200) NOT NULL,
  `crt_time` datetime DEFAULT NULL,
  `invalid_time` datetime DEFAULT NULL,
  `is_valid` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='access_token';

-- ----------------------------
-- Table structure for app_notice
-- ----------------------------
DROP TABLE IF EXISTS `app_notice`;
CREATE TABLE `app_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL COMMENT '公告内容',
  `crt_time` datetime DEFAULT NULL,
  `del_flag` int(1) NOT NULL COMMENT '是否删除',
  `type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- ----------------------------
-- Table structure for song_rank_data
-- ----------------------------
DROP TABLE IF EXISTS `song_rank_data`;
CREATE TABLE `song_rank_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_record_id` varchar(64) NOT NULL COMMENT '定时实例id',
  `rank` int(3) NOT NULL COMMENT '歌曲排行',
  `song` varchar(255) DEFAULT NULL COMMENT '歌曲名称',
  `singer` varchar(255) DEFAULT NULL COMMENT '歌手名称',
  `ratio` varchar(5) NOT NULL COMMENT '占比',
  `pic_url` varchar(255) DEFAULT NULL,
  `song_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37017 DEFAULT CHARSET=utf8mb4 COMMENT='排行数据表';

-- ----------------------------
-- Table structure for song_rank_data_diff
-- ----------------------------
DROP TABLE IF EXISTS `song_rank_data_diff`;
CREATE TABLE `song_rank_data_diff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_record_id` varchar(64) NOT NULL COMMENT '定时实例id',
  `rank_change` int(3) NOT NULL COMMENT '排行变化',
  `change_time` datetime DEFAULT NULL COMMENT '记录时间',
  `song` varchar(255) DEFAULT NULL COMMENT '歌曲名称',
  `singer` varchar(255) DEFAULT NULL COMMENT '歌手名称',
  `target_userid` varchar(255) DEFAULT NULL COMMENT '目标昵称',
  `is_batch_update` int(1) NOT NULL COMMENT '是否系统批量插入',
  `pic_url` varchar(255) DEFAULT NULL,
  `song_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14949 DEFAULT CHARSET=utf8mb4 COMMENT='排行变化表';

-- ----------------------------
-- Table structure for song_rank_job
-- ----------------------------
DROP TABLE IF EXISTS `song_rank_job`;
CREATE TABLE `song_rank_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(2) NOT NULL,
  `job_name` varchar(50) DEFAULT NULL,
  `job_group` varchar(20) DEFAULT NULL,
  `job_type` varchar(100) NOT NULL,
  `cron_expression` varchar(32) NOT NULL,
  `target_user_id` varchar(32) NOT NULL,
  `target_nickname` varchar(100) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  `upd_user` varchar(255) DEFAULT NULL,
  `upd_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `job_name_job_group` (`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=1760 DEFAULT CHARSET=utf8mb4 COMMENT='定时任务表';

-- ----------------------------
-- Table structure for song_rank_job_record
-- ----------------------------
DROP TABLE IF EXISTS `song_rank_job_record`;
CREATE TABLE `song_rank_job_record` (
  `id` varchar(64) NOT NULL,
  `job_id` int(11) NOT NULL COMMENT '定时任务id',
  `start_time` datetime NOT NULL COMMENT '执行开始时间',
  `end_time` datetime NOT NULL COMMENT '执行结束时间',
  `new_data` int(1) NOT NULL COMMENT '数据是否变化',
  `snapshot` varchar(255) NOT NULL COMMENT '数据快照',
  `count` int(9) NOT NULL COMMENT '总播放数',
  PRIMARY KEY (`id`),
  KEY `idx_jobid_newdata` (`job_id`,`new_data`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务执行详情表';

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
  `page` varchar(255) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `is_valid` int(1) DEFAULT '1',
  `upd_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='模板消息推送';

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
  `del_flag` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='用户任务关联表';

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(64) NOT NULL COMMENT '昵称',
  `gender` int(1) DEFAULT '1' COMMENT '性别',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像',
  `country` varchar(100) DEFAULT NULL COMMENT '国家',
  `province` varchar(100) DEFAULT NULL COMMENT '省份',
  `city` varchar(100) DEFAULT NULL COMMENT '城市',
  `language` varchar(20) DEFAULT NULL COMMENT '语言',
  `openid` varchar(50) NOT NULL,
  `job_limit` int(3) DEFAULT '3' COMMENT '关注上限数量',
  `crt_time` datetime DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';
