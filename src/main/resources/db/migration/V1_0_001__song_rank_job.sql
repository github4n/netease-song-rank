-- ----------------------------
-- Table structure for song_rank_job
-- ----------------------------
DROP TABLE IF EXISTS `song_rank_job`;
CREATE TABLE `song_rank_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(2) NOT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `job_group` varchar(100) DEFAULT NULL,
  `job_type` varchar(100) NOT NULL,
  `cron_expression` varchar(32) NOT NULL,
  `target_userid` varchar(32) NOT NULL,
  `target_nickname` varchar(100) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  `upd_user` varchar(255) DEFAULT NULL,
  `upd_name` varchar(255) DEFAULT NULL,
  UNIQUE KEY `job_name_job_group` (`job_name`,`job_group`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '定时任务表';

-- ----------------------------
-- Records of song_rank_job
-- ----------------------------
INSERT INTO `song_rank_job` VALUES ('1', '1', '王跳跳的听歌排行爬虫', 'group1', '听歌排行爬取任务', '0 */1 * * * ?', '1301208430', '王跳跳4399', null, null, null, null, null, null, null);
INSERT INTO `song_rank_job` VALUES ('2', '1', 'red的听歌排行爬虫', 'group1', '听歌排行爬取任务', '0 */1 * * * ?', '33255454', '半赫', null, null, null, null, null, null, null);
INSERT INTO `song_rank_job` VALUES ('3', '1', '千百个迷人愿望的听歌排行爬虫', 'group1', '听歌排行爬取任务', '0 */1 * * * ?', '70142586', '千百个迷人愿望', null, null, null, null, null, null, null);
