package com.llvision.face.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.constants.Constants;
import com.llvision.face.dao.biz.SystemBiz;
import com.llvision.face.dao.mapper.PersonMapper;
import com.llvision.face.entity.Person;
import com.llvision.face.entity.SystemConf;
import com.llvision.face.function.RecordFunction;
import com.llvision.face.utils.StringUtils;
import com.llvision.face.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 标准人脸识别
 * 
 * @author wangwt
 *
 */
@Service
public class StandardFaceRecordService {
	private final Logger log = LoggerFactory.getLogger(StandardFaceRecordService.class);
	@Resource
	private PersonMapper personMapper;
	@Resource
	private CooperationService cooperationService;
	@Resource
	private SystemBiz systemBiz;
	@Resource
    private RecordFunction recordFunction;

    @Value("${pic_url}")
    private String pic_url;

	public Result standardFaceRecord(Integer userId, String results, String sourcePicBase64, String bgImageId,
			String longitude, String latitude, HttpServletRequest request, String appRecordId,String appId) {
		log.info("请求开始>>>>>");
		long startTime = System.currentTimeMillis();
		RequestContext requestContext = new RequestContext(request);
		Result r = new Result();
		int thisId;
		// 识别照片处理
		String pathName = cooperationService.saveImageFromBase64(sourcePicBase64);
		JSONArray resultList = JSON.parseArray(results);
		SystemConf conf = new SystemConf().setAppId(appId);
		SystemConf systemConf = systemBiz.selectOne(conf);

		Integer congfigSimilarity = systemConf != null ? Integer.valueOf(systemConf.getSimilarity()) : 80;
		thisId = cooperationService.cooperationRecord(resultList,pathName,congfigSimilarity,userId,bgImageId,longitude,latitude,appRecordId,appId);
        Map<String, Object> recordData = cooperationService.createRecordData(thisId, request, congfigSimilarity,appId);
        if(null!=resultList&&resultList.size()>0) cooperationService.createLikeList(resultList,appId);
        Map<String, Object> data = new HashMap<>();
        data.put("record",recordData);
        data.put("faceLike",resultList);
		r.setMsg(requestContext.getMessage("qqcg"));
		r.setCode(0);
		r.setData(data);
// 		保存相似结果
        if (!StringUtils.isEmpty(resultList)) {
            final JSONArray list1 = resultList;
            final int id2 = thisId;
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> recordFunction.saveLikeRecord(list1, id2,appId));
            executorService.shutdown();
        }
		long endTime = System.currentTimeMillis();
		log.info("时长：>>>>>" + (endTime - startTime) + "<<<<<");
		return r;
	}


}
