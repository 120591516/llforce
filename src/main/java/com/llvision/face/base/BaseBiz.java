package com.llvision.face.base;

import com.llvision.face.entity.SystemConf;
import com.llvision.face.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基础biz,即service
 * @Author yudong
 * @Date 2018年07月05日 下午3:39
 */
public abstract class BaseBiz<M extends BaseMapper<T>,T>{

    @Autowired
    protected M mapper;


    /**
     * 根据条件查询单个
     * @param entity
     * @return
     */
    public T selectOne(T entity){
        return mapper.selectOne(entity);
    }

    /**
     * 根据条件查询多个
     * @param entity
     * @return
     */
    public List<T> select(T entity){
        return mapper.select(entity);
    }

    /**
     * 查询全部
     * @return
     */
    public List<T> selectAll(){
        return mapper.selectAll();
    }

    /**
     * 添加单个实体
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(T entity, BusinessException ex){
        try {
            mapper.insertSelective(entity);
        }catch (Exception e){
            e.printStackTrace();
            throw ex;
        }
    }

    /**
     * 删除该实体所有信息
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(T entity, BusinessException ex) {
        try {
        mapper.delete(entity);
      }catch (Exception e){
          throw ex;
      }
    }

    /**
     * 根据主键删除所有信息
     * @param key
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByPrimaryKey(Object key, BusinessException ex){
        try {
        mapper.deleteByPrimaryKey(key);
        }catch (Exception e){
            throw ex;
        }
    }


    /**
     * 根据主键修改实体
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSelectiveById(T entity, BusinessException ex) {
        try {
        mapper.updateByPrimaryKeySelective(entity);
       }catch (Exception e){
        throw ex;
      }
    }


    public T selectByPrimaryKey(Object primaryKey) {
       return mapper.selectByPrimaryKey(primaryKey);
    }
}
