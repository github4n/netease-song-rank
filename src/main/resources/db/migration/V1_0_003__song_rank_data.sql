-- ----------------------------------
-- Table structure for song_rank_data
-- ----------------------------------
DROP TABLE IF EXISTS `song_rank_data`;
CREATE TABLE `song_rank_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_record_id` varchar(64) NOT NULL COMMENT '定时实例id',
  `rank` int(3) NOT NULL COMMENT '歌曲排行',
  `song` varchar(255) DEFAULT NULL COMMENT '歌曲名称',
  `singer` varchar(255) DEFAULT NULL COMMENT '歌手名称',
  `ratio` varchar(5) NOT NULL COMMENT '占比',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT = '排行数据表';