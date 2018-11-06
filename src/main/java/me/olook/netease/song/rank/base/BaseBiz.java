package me.olook.netease.song.rank.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-02-08 14:34
 */
public abstract class BaseBiz<M extends Mapper<T>, T> {
    @Autowired
    protected M mapper;

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(T entity) {
        return mapper.selectOne(entity);
    }


    public T selectById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }


    public List<T> selectList(T entity) {
        return mapper.select(entity);
    }


    public List<T> selectListAll() {
        return mapper.selectAll();
    }


    public Long selectCount(T entity) {
        return (long) mapper.selectCount(entity);
    }


    public int insert(T entity) {

        try{
            //EntityUtils.setCreatAndUpdatInfo(entity);
            return mapper.insert(entity);
        }catch (DuplicateKeyException e){//唯一键重复
            return -1;
        }
    }

    /**
     * 在使用线程池的线程调用insert方法会报错，此方法是替代方案
     * @param entity
     * @return
     */
    public int insertWithoutOperation(T entity){
        try{
            return mapper.insert(entity);
        }catch (DuplicateKeyException e){//唯一键重复
            return -1;
        }
    }


    public int insertSelective(T entity) {
        try{
            //EntityUtils.setCreatAndUpdatInfo(entity);
            return mapper.insertSelective(entity);
        }catch (DuplicateKeyException e){//唯一键重复
            return -1;
        }

    }


    public int delete(T entity) {
        return mapper.delete(entity);
    }


    public int deleteById(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }


    public int updateById(T entity) {
        //EntityUtils.setUpdatedInfo(entity);
        return mapper.updateByPrimaryKey(entity);
    }


    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    public int updateSelectiveById(T entity) {
        //EntityUtils.setUpdatedInfo(entity);
        return mapper.updateByPrimaryKeySelective(entity);
    }

    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }

}
