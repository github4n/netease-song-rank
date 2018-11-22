# netease-song-rank


## 更新内容
- Spring Boot version 1.5.7.RELEASE -> 2.0.5.RELEASE
- 弃用Mybatis及Mapper，使用Spring Data JPA
- Druid 使用Spring Boot starter，弃用Flyway
- 修改排行榜变化查找算法，新增通过score查找方式
- 任务清理、模板清理任务作为内置定时任务
- 代理池算法及数据结构调整（双队列）
- 减少不必要的api暴露
- 代码精简优化

## 错误排除
1. songRankDiff插入整榜  
SongRankData 的record id与最后一次record id不符合，导致查出SongRankData为空

2. songRankDiff插入大量数据  
算法排行或score比较条件写反