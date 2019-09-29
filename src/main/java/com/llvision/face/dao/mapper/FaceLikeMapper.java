package com.llvision.face.dao.mapper;

import com.llvision.face.base.BaseMapper;
import com.llvision.face.entity.FaceLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FaceLikeMapper extends BaseMapper<FaceLike> {

    @Select("select * from face_like where face_recoed_id = #{faceRecordId}  and is_del!=1  order by  similarity desc ")
    @ResultMap("BaseResultMap")
    List<FaceLike> getListByFaceId(@Param("faceRecordId") int faceRecordId);
}