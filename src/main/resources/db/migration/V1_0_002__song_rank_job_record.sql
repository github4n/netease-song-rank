-- ----------------------------------
-- Table structure for song_rank_job_record
-- ----------------------------------
DROP TABLE IF EXISTS `song_rank_job_record`;
CREATE TABLE `song_rank_job_record` (
  `id` varchar(64) NOT NULL,
  `job_id` int(11) NOT NULL COMMENT '定时任务id',
  `start_time` datetime NOT NULL COMMENT '执行开始时间',
  `end_time` datetime NOT NULL COMMENT '执行结束时间',
  `new_data` int(1) NOT NULL COMMENT '数据是否变化',
  `snapshot` varchar(255) NOT NULL COMMENT '数据快照',
  `count` int(9) NOT NULL COMMENT '总播放数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '定时任务执行详情表';