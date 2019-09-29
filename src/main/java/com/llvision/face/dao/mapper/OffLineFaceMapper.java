package com.llvision.face.dao.mapper;

import com.llvision.face.base.BaseMapper;
import com.llvision.face.entity.OffLineFace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OffLineFaceMapper extends BaseMapper<OffLineFace> {

    @Select("select count(*) from offline_face where create_user=#{userId} and name=#{name} and is_del!=1 and `type`=#{type} and app_id = #{appId}")
    int checkDelByUserIdAndName(@Param("userId") int userId, @Param("name") String name, @Param("type") int type,@Param("appId") String appId);

    OffLineFace getOfflineFaceValidByIdAndAppId(@Param("id") Integer id,String appId);

    @Update("update offline_face set is_del=1 where id = #{id} and app_id = #{appId}")
    int delOfflineFace(@Param("id") int id,@Param("appId") String appId);

    List<OffLineFace> selectOfflineFaces(@Param("userId") int userId,@Param("type") int type,@Param("appId") String appId);

    Long offLineFacePersonCount(@Param("appId") String appId);

    @Select("SELECT DISTINCT\n" +
            "\twf.lib_id\n" +
            "FROM\n" +
            "\twarehouse_face wf\n" +
            "LEFT JOIN offline_face ofl ON wf.lib_id = ofl.id\n" +
            "LEFT JOIN person p ON wf.person_id = p.id\n" +
            "WHERE\n" +
            "\tofl.create_user = #{userId} AND ofl.is_del = 0 AND ofl.type =#{type}  AND ofl. STATUS = 1 AND wf.is_del != 1\n" +
            "AND p.is_del != 1 AND ofl.app_id = #{appId}")
    List<Integer> getAvailableLibIds(@Param("userId") Integer userId, @Param("appId") String appId, @Param("type") int type);
}