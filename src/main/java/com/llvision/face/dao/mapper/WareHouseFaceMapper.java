package com.llvision.face.dao.mapper;

import com.llvision.face.base.BaseMapper;
import com.llvision.face.entity.WareHouseFace;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WareHouseFaceMapper extends BaseMapper<WareHouseFace> {
    @Select("select * from warehouse_face where lib_id=#{libId}  and is_del!=1  ")
    @ResultMap("BaseResultMap")
    List<WareHouseFace> getListByWarehouseId(@Param("libId") int warehouseId);


    @Update("update warehouse_face set is_del=1 where person_id=#{personId} ")
    int delByPersonId(@Param("personId") long personId);

    @Update("update warehouse_face set is_del=1 where lib_id=#{libId} ")
    int delByLibId(@Param("libId") long libId);
}