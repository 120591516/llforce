package com.llvision.face.base;


import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用的mapper 这个接口不能被扫描到
 * 详细情况请百度:tk.mybatis
 * @Author yudong
 * @Date 2018年07月02日16:22:50
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
