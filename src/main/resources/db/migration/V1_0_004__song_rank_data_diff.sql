-- ----------------------------------
-- Table structure for song_rank_data_diff
-- ----------------------------------
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '排行变化表';