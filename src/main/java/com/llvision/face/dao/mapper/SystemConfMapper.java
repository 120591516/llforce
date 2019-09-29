package com.llvision.face.dao.mapper;

import com.llvision.face.base.BaseMapper;
import com.llvision.face.entity.SystemConf;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemConfMapper extends BaseMapper<SystemConf> {

    int updateSystemConfig(SystemConf systemConf);
}