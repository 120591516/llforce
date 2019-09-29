package com.llvision.face.dao.mapper;

import com.llvision.face.base.BaseMapper;
import com.llvision.face.entity.Person;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface PersonMapper extends BaseMapper<Person> {

    Person selectPersonValidByIdAndAppId(@Param("id") Long id,@Param("appId") String appId);

    @Select("SELECT CASE WHEN  max(mark) IS NULL THEN 0 ELSE  max(mark) END mark FROM person;")
    int getMaxMark();

    @Select("SELECT  count(*) FROM person as a  left join warehouse_face as b  on a.id=b.person_id where b.lib_id=#{warehouseId} and a.is_del!=1 and b.is_del!=1 ")
    int getPersonCountBywarehouseId(@Param("warehouseId") int warehouseId);

    @Select("SELECT a.* " +
            "FROM person as a  " +
            "left join warehouse_face as b  on a.id=b.person_id" +
            " where b.lib_id=#{libId}  and a.is_del!=1 and b.is_del!=1 order by a.create_time desc limit #{offset},#{limit} ")
    @ResultMap("BaseResultMap")
    List<Person> getPageListBywarehouseId(@Param("libId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    List<Person> getPersonByPersonAndWarehouseId(@Param("person") Person person, @Param("warehouseId")Integer warehouseId);

    @Select("SELECT wu.warehouse_id warehouseId,p.id personId,IFNULL(p.pic,'') pic,IFNULL(p.`name`,'') personName,IFNULL(p.card,'')  personCard ,IFNULL(p.sex,2) sex ,IFNULL(p.address,'') address ,IFNULL(p.birthday,'') birthday,p.is_warning isWarning, IFNULL(p.`hash`,'') `hash`, IFNULL(p.model,'') model FROM  warehouse_user wu LEFT JOIN offline_face ofa ON wu.warehouse_id = ofa.id AND ofa.type = 1 AND ofa.is_del = 0 LEFT JOIN warehouse_face wf ON wu.warehouse_id = wf.lib_id LEFT JOIN person p ON wf.person_id = p.id WHERE wf.is_del = 0 AND p.is_del =0 AND wu.user_id =#{personId} AND ofa.status = 1 AND ofa.app_id =#{appId}")
    @ResultType(HashMap.class)
    List<Map<String,Object>> getPersonListByUserId(@Param("personId") Integer personId,@Param("appId") String appId);

    List<Person> getPersonByPseron(Person person);
}