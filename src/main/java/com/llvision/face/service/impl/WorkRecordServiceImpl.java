package com.llvision.face.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.llvision.face.service.WatchDogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.constants.Constants;
import com.llvision.face.dao.biz.FaceLikeBiz;
import com.llvision.face.dao.biz.FaceRecordBiz;
import com.llvision.face.dao.biz.PersonBiz;
import com.llvision.face.dao.biz.SystemBiz;
import com.llvision.face.entity.FaceLike;
import com.llvision.face.entity.FaceRecord;
import com.llvision.face.entity.Person;
import com.llvision.face.entity.SystemConf;
import com.llvision.face.service.IWorkRecordService;
import com.llvision.face.service.StandardFaceRecordService;
import com.llvision.face.utils.CollectionUtils;
import com.llvision.face.utils.DateUtils;
import com.llvision.face.utils.PagedModelList;
import com.llvision.face.utils.StringUtils;
import com.llvision.face.vo.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangwenteng
 *
 */
@Service
@Transactional
@Slf4j
public class WorkRecordServiceImpl implements IWorkRecordService {

	@Resource
	private StandardFaceRecordService standardFaceRecordService;

	@Resource
	private SystemBiz systemBiz;

	@Resource
	private FaceRecordBiz faceRecordBiz;

	@Resource
	private FaceLikeBiz faceLikeBiz;

	@Resource
	private PersonBiz personBiz;

	@Resource
	private Llvision00052RecordService llvision00052RecordService;

	@Resource
	private WatchDogService watchDogService;

	@Value("${pic_url}")
	private String pic_url;

	private Integer compareMode;
	@Override
	public Result faceRecord(Integer userId, String appId, String results, String sourcePicBase64,String landmarkJSON,String imageId,HttpServletRequest request, String appRecordId) {
		Result r = null;
		log.info("compareMode:>>>>"+compareMode);
		if(null ==compareMode){
			compareMode = (int)watchDogService.decryptFile("compareMode");
		}
		if (compareMode == 1) {
			r = standardFaceRecordService.standardFaceRecord(userId, results, sourcePicBase64, "", "", "", request, appRecordId,appId);
		}else if (compareMode == 2||compareMode == 3) {
			if(Constants.CUSTOMER.equals("llvision00052")){
				r =	llvision00052RecordService.faceRecord(userId, sourcePicBase64,landmarkJSON,imageId, "", "", "", request, appRecordId,appId);
			}
		}else{
			RequestContext requestContext = new RequestContext(request);
			r = new Result();
			r.setMsg(requestContext.getMessage("zwsj"));
			r.setCode(0);
			r.setData(new JSONObject());
		}
		JSONObject resultJson = (JSONObject) JSON.toJSON(r);
		log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
		return r;
	}

	@Override
	public Result faceRecordPagingList(int userId, String appId, Integer pageNum, Integer pageSize, HttpServletRequest request) {
		RequestContext requestContext = new RequestContext(request);
		Result r = new Result();
		SystemConf systemConf = new SystemConf().setAppId(appId);
		systemConf = systemBiz.selectOne(systemConf);
		Integer congfigSimilarity = systemConf != null ? Integer.valueOf(systemConf.getSimilarity()) : 80;
		Map<String, Object> map = new HashMap<>();
		StringBuilder where = new StringBuilder();
		where.append("  fr.parent_id=0 ");
		where.append("  and fr.app_id = '");
		where.append(appId);
		where.append("'");
		if (userId != 0) {  //普通用户返回自己创建的数据
			where.append(" and fr.create_user = ");
			where.append(userId);
		}
		PagedModelList<FaceRecord> pml = faceRecordBiz.getAppPageListByWhere(where.toString(),pageNum, pageSize);
		List<Map<String, Object>> maplist = new ArrayList<>();
		if (!CollectionUtils.isEmpty(pml.getPagedModelList())) {
			for (FaceRecord fr : pml.getPagedModelList()) {
				Map<String, Object> data = new HashMap<>();
				data.put("id", fr.getId());
				data.put("appRecordId",fr.getAppRecordId());
				data.put("pic", pic_url + fr.getPic());
				data.put("comparePic",
							StringUtils.isNotEmpty(fr.getComparePic()) ?pic_url+ fr.getComparePic() : "");
				data.put("similarity", fr.getSimilarity());
				if (fr.getSimilarity() >= congfigSimilarity) {
					if (fr.getPersonId() != 0) {
						data.put("personName", fr.getPersonName());
						data.put("personCard", fr.getPersonCard());
					} else {
						data.put("personName", requestContext.getMessage("wz"));
						data.put("personCard", requestContext.getMessage("wz"));
					}
					data.put("isInLib", 1);
				} else {
					data.put("isInLib", 0);
					data.put("personName", requestContext.getMessage("wz"));
					data.put("personCard", requestContext.getMessage("bzkz"));
				}
				data.put("isWarning", fr.getIsWarning());
				data.put("personId", fr.getPersonId());
//				String warningData = fr.getWarningType();
//				if (!StringUtils.isEmpty(warningData)) {
//					List<Integer> warningList = WarningUtils.getWarningList(warningData);
//					for (int i = 0; i < warningList.size(); i++) {
//						if (warningList.get(i) == 2) {
//							warningList.remove(i);
//						}
//					}
//					data.put("face_warning", warningList);
//				} else {
//					data.put("face_warning", null);
//				}
				data.put("createTime", DateUtils.dateTimeToString(fr.getLastRecordTime()));
				maplist.add(data);
			}
		}
		map.put("faceList", maplist);
		map.put("pageTotal", pml.getPageCount());
		map.put("pageNum", pageNum);
		map.put("total", pml.getTotalCount());
		map.put("userId", userId);
		r.setCode(0);
		r.setMsg(requestContext.getMessage("qqcg"));
		r.setData(map);
		JSONObject resultJson = (JSONObject) JSON.toJSON(r);
		log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
		return r;
	}

	@Override
	public Result faceRecordInfo(int userId, String appId, Integer recordId, HttpServletRequest request,String appRecordId) {
		RequestContext requestContext = new RequestContext(request);
		Result r = new Result();
		Map<String, Object> map = new HashMap<>();
		FaceRecord query = new FaceRecord();
		if (recordId == null) {
			if (StringUtils.isEmpty(appRecordId)) {
				r.setCode(0);
				r.setMsg(requestContext.getMessage("cscw"));
				r.setData(map);
				return r;
			}
		}
		FaceRecord faceRecord = faceRecordBiz.selectInfo(appRecordId,recordId,appId);
		List<Map<String, Object>> faceLikeMaplist = new ArrayList<>();
		Map<String, Object> data = null;
		Person person =null;
		if (faceRecord != null) {
			data = new HashMap<>();
			data.put("id", faceRecord.getId());
			data.put("appRecordId",faceRecord.getAppRecordId());
			data.put("pic", pic_url + faceRecord.getPic());
			data.put("comparePic",
					StringUtils.isNotEmpty(faceRecord.getComparePic()) ?pic_url+ faceRecord.getComparePic() : "");
//			data.put("bgImage", StringUtils.isNotEmpty(faceRecord.getBgImage()) ? Constants.getIpAddress(request) + faceRecord.getBgImage() : "");
			data.put("similarity", faceRecord.getSimilarity());
			data.put("personName", faceRecord.getPersonName());
			data.put("isWarning", faceRecord.getIsWarning());
			data.put("personCard", faceRecord.getPersonCard());
			data.put("personId", faceRecord.getPersonId());
			data.put("createTime", DateUtils.dateTimeToString(faceRecord.getLastRecordTime()));
			if (faceRecord.getPersonId()!=0) {
				data.put("isInLib", 1);
			} else {
				data.put("isInLib", 0);
			}
			List<FaceLike> faceLikeList = null;
			if(recordId!=null){
				List<FaceRecord> faceRecordList = faceRecordBiz.getListByParentIdSimilarityDesc(faceRecord.getId());
				if (!CollectionUtils.isEmpty(faceRecordList)) {
					faceLikeList = faceLikeBiz.getListByFaceRecordId(faceRecordList.get(0).getId());
				}
			}else{
				faceLikeList = faceLikeBiz.getListByFaceRecordId(faceRecord.getId());
			}
			//更具识别记录获取相似度列表
			if (!CollectionUtils.isEmpty(faceLikeList)) {
				for (FaceLike a : faceLikeList) {
					Map<String, Object> faceLikeData = new HashMap<>();
					faceLikeData.put("pic", StringUtils.isNotEmpty(a.getComparePic())?pic_url + a.getComparePic():"");
					person = personBiz.selectByPrimaryKey(a.getPersonId());
					if (person != null) {
						faceLikeData.put("personName",person.getName());
						faceLikeData.put("personCard",person.getCard());
						faceLikeData.put("sex",person.getSex());
						faceLikeData.put("isWarning",person.getIsWarning());
						faceLikeData.put("address",person.getAddress()==null?"":person.getAddress());
						faceLikeData.put("birthday",person.getBirthday()==null?"":person.getBirthday());
					}else{
						faceLikeData.put("personName","");
						faceLikeData.put("personCard","");
						faceLikeData.put("sex",2);
						faceLikeData.put("isWarning",0);
						faceLikeData.put("address","");
						faceLikeData.put("birthday","");
					}
					faceLikeData.put("similarity", a.getSimilarity());
					faceLikeData.put("personId", a.getPersonId());
					faceLikeData.put("isWarning", a.getStatus());
					faceLikeMaplist.add(faceLikeData);
				}
			}
		}
		map.put("record", data);
		map.put("faceLike", faceLikeMaplist);
		map.put("userId", userId);
		r.setCode(0);
		r.setMsg(requestContext.getMessage("qqcg"));
		r.setData(map);
		JSONObject resultJson = (JSONObject) JSON.toJSON(r);
		log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
		return  r;
	}
}
