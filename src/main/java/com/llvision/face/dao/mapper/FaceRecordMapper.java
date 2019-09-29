package com.llvision.face.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.llvision.face.base.BaseMapper;
import com.llvision.face.entity.FaceRecord;

@Mapper
public interface FaceRecordMapper extends BaseMapper<FaceRecord> {

	@Select("select * from face_record where create_user = #{createUser} and is_del!=1 order by id desc limit 2 FOR UPDATE")
	@ResultMap("BaseResultMap")
	List<FaceRecord> getNewListByCreateUser(@Param("createUser") Long createUser);

	@Select(" select count(*) from face_record fr  where  ${strWhere} and fr.is_del!=1 ")
    int getCountByWhere(@Param("strWhere") String strWhere);

	@Select(" select fr.* from face_record  fr  where ${strWhere}  and fr.is_del!=1  order by fr.id desc  limit #{offset} , #{limit} ")
	@ResultMap("BaseResultMap")
	List<FaceRecord> getPageListByWhere(@Param("strWhere") String strWhere, @Param("offset") int offset, @Param("limit") int limit);

	@Select("SELECT * FROM face_record where parent_id = #{parentId} order by similarity and is_del!=1  desc")
	@ResultMap("BaseResultMap")
	List<FaceRecord> getListByParentIdSimilarityDesc(@Param("parentId") Integer parentId);

	@Select("SELECT fl.person_id personId , fl.similarity FROM `face_record` fr LEFT JOIN face_like fl ON fr.id = fl.face_recoed_id WHERE fr.pic LIKE CONCAT('%',#{resultImageName}) AND parent_id !=0")
	List<Map<String, Object>> getResultlist(@Param("resultImageName") String resultImageName);

    FaceRecord selectInfo(@Param("appRecordId") String appRecordId, @Param("recordId") Integer recordId, @Param("appId") String appId);

    @Select(" SELECT * FROM face_record  WHERE id =#{parentId}  FOR UPDATE")
	@ResultMap("BaseResultMap")
    FaceRecord getByPrimaryKey(@Param("parentId") Integer parentId);

	@Select("SELECT fl.person_id faceId , fl.similarity FROM `face_record` fr LEFT JOIN face_like fl ON fr.id = fl.face_recoed_id WHERE fr.pic =#{resultImageName} AND parent_id !=0 FOR UPDATE")
	List<Map<String, Object>> getResultList(@Param("resultImageName") String resultImageName);
}