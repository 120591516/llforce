package com.llvision.face.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.dao.biz.PersonBiz;
import com.llvision.face.dao.biz.SystemBiz;
import com.llvision.face.entity.Person;
import com.llvision.face.entity.SystemConf;
import com.llvision.face.function.RecordFunction;
import com.llvision.face.mqtt.SendMessage;
import com.llvision.face.service.CooperationService;
import com.llvision.face.service.IOffLineFaceService;
import com.llvision.face.utils.CollectionUtils;
import com.llvision.face.utils.StringUtils;
import com.llvision.face.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lifan on 2019/6/17.
 */
@Service
@Transactional
@Slf4j
public class Llvision00052RecordService {

    @Resource
    private Llvision00052Service llvision00052Service;
    @Resource
    private SystemBiz systemBiz;
    @Resource
    private IOffLineFaceService iOffLineFaceService;
    @Resource
    private RecordFunction recordFunction;
    @Resource
    private CooperationService cooperationService;

    @Resource
    private PersonBiz personBiz;

	@Resource
    private SendMessage sendMessage;
    @Value("${duplicateRemoval.flag}")
    private Boolean duplicateRemovalFlag;

    public Result faceRecord(Integer userId, String sourcePicBase64,String landmarkJSON, String imageId,String bgImageId,
                             String longitude, String latitude, HttpServletRequest request, String appRecordId,String appId)  {
        log.info("llvision00052请求开始>>>>>");
        long startTime = System.currentTimeMillis();
        Result r = new Result();
        RequestContext requestContext = new RequestContext(request);
        JSONArray resultList = new JSONArray();
        int thisId = 0;
        // 识别照片处理
        String pathName = cooperationService.saveImageFromBase64(sourcePicBase64);
        // 获取系统配置的相似度阈值
        SystemConf conf = new SystemConf().setAppId(appId);
        SystemConf systemConf = systemBiz.selectOne(conf);
        Integer congfigSimilarity = systemConf != null ? Integer.valueOf(systemConf.getSimilarity()) : 80;
//        Boolean flag = false;
//        if (duplicateRemovalFlag) {
//            String imageName = pathName.replace(Constants.FACE_RECORD_PIC_URL + "/", "");
//			String duplicateRemoval = duplicateRemovalService.duplicateRemoval(userId, imageName,
//                    sourcePicBase64, landmarkJSON);
//            JSONObject json = JSONObject.parseObject(duplicateRemoval);
//            if (json.getBooleanValue("isExist")) {
//                String resultImageName = json.getString("name");
//                List<Map<String, Object>> faceLike = faceRecordBiz.getResultList(resultImageName);
//                log.info("faceLike" + JSON.toJSONString(faceLike));
//                log.info("CollectionUtils.isEmpty(faceLike)||(faceLike.size()>0&&faceLike.get(0)==null)"+(CollectionUtils.isEmpty(faceLike)||(faceLike.size()>0&&faceLike.get(0)==null)));
//				if (CollectionUtils.isEmpty(faceLike)||(faceLike.size()>0&&faceLike.get(0)==null)) {
//					flag = true;
//                } else {
//                    resultList = JSONArray.parseArray(JSON.toJSONString(faceLike));
//                }
//            } else {
//                flag = true;
//            }
//		} else {
//			flag = true;
//		}
//        if(flag){
//            String faceList = llvision00052Service.compareFaceLandmark(sourcePicBase64,landmarkJSON);
//            List<Map<String, Object>> personList = getCompareFaceResult(userId, request, faceList,appId);
//            log.info("personList:" + JSON.toJSONString(personList));
//            resultList = JSON.parseArray(JSONObject.toJSONString(personList));
//            log.info("resultList:" + resultList);
//        }
        resultList = getResultList(userId, sourcePicBase64, landmarkJSON, resultList, pathName,appId);
        log.info("resultList:" + resultList);
        thisId = cooperationService.cooperationRecord(resultList,pathName,congfigSimilarity,userId,bgImageId,longitude,latitude,appRecordId,appId);
        Map<String, Object> recordData = cooperationService.createRecordData(thisId, request, congfigSimilarity,appId);
        if(null!=resultList&&resultList.size()>0) cooperationService.createLikeList(resultList,appId);
        Map<String, Object> data = new HashMap<>();
        data.put("record",recordData);
        data.put("faceLike",resultList);
        data.put("imageId",imageId);
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
		// sendMessage.send(JSON.toJSONString(r),"");
        return r;

    }

    private List<Map<String, Object>> getCompareFaceResult(String faceList,String appId){
        List<Map<String, Object>> personList = new ArrayList<>();
        long jsonStart = System.currentTimeMillis();
        JSONArray comparisonList = JSON.parseArray(faceList);
        if (comparisonList != null && !CollectionUtils.isEmpty(comparisonList)) {
            // 人脸比对结果
            Map<String, Object> dataMap;
            JSONObject ob;
            int similarity;
            for (int i = 0; i < comparisonList.size(); i++){
                dataMap = new HashMap<>();
                ob = comparisonList.getJSONObject(i);
                String faceId = ob.getString("name");
                Person person = personBiz.getPersonByIdAndAppId(ob.getLongValue("name"),appId);
                if (person == null) {
                    continue;
                }
				Double parseDouble = Double.parseDouble(ob.getString("value"));
                similarity = (int) (parseDouble * 100D);
				dataMap.put("personId", faceId);
                dataMap.put("similarity", (double) similarity);
                personList.add(dataMap);
            }
        } else {
            personList = null;
        }
        long jsonEnd = System.currentTimeMillis();
        System.out.println("json解析时长：" + (jsonEnd - jsonStart));
        return personList;
    }

    public JSONArray getResultList(Integer userId, String sourcePicBase64, String landmarkJSON, JSONArray resultList, String pathName, String appId){
        resultList = cooperationService.compareToDuplicateRemovel(userId, sourcePicBase64, landmarkJSON, resultList, pathName);
        if (null == resultList) {
            List<Integer> availableLibIds = iOffLineFaceService.getAvailableLibIds(userId,appId, 1);
            String faceList = llvision00052Service.compareFaceLandmark(sourcePicBase64, landmarkJSON, availableLibIds);
            List<Map<String, Object>> personList = getCompareFaceResult(faceList,appId);
            //三方识别
            resultList = JSON.parseArray(JSONObject.toJSONString(personList));
        }
        return resultList;
    }
}
