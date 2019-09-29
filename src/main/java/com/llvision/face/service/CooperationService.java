package com.llvision.face.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.config.SftpConfig;
import com.llvision.face.constants.Constants;
import com.llvision.face.dao.mapper.FaceRecordMapper;
import com.llvision.face.dao.mapper.PersonMapper;
import com.llvision.face.entity.FaceRecord;
import com.llvision.face.entity.Person;
import com.llvision.face.function.RecordFunction;
import com.llvision.face.utils.CollectionUtils;
import com.llvision.face.utils.DateUtils;
import com.llvision.face.utils.SFTPUtil;
import com.llvision.face.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class CooperationService {
	@Resource
	private SftpConfig sftpConfig;
    @Resource
    private RecordFunction recordFunction;
    @Resource
    private PersonMapper personMapper;
    @Resource
    private FaceRecordMapper faceRecordMapper;

    @Resource
    private DuplicateRemovalService duplicateRemovalService;

    @Value("${duplicateRemoval.flag}")
    private Boolean duplicateRemovalFlag;

    @Value("${pic_url}")
    private String pic_url;

	public String saveImageFromBase64(String sourcePicBase64) {
        String num = UUID.randomUUID().toString();
		String fileName = num + ".jpg";
		String filePath = Constants.FACE_RECORD_PIC_URL + "/";
		SFTPUtil sftpUtil = new SFTPUtil(sftpConfig.getHost(), sftpConfig.getUsername(), sftpConfig.getPassword(),
				sftpConfig.getPort());
        String newSourcePicBase64 = sourcePicBase64.replace("\n", "");
        newSourcePicBase64 = newSourcePicBase64.replace("\r", "");
		byte[] decode = Base64.getDecoder().decode(newSourcePicBase64);
		InputStream input = new ByteArrayInputStream(decode);
		sftpUtil.connect();
		sftpUtil.upload(filePath, fileName, input);
		sftpUtil.disconnect();
		return filePath + fileName;
    }

    public int cooperationRecord(JSONArray resultList, String pathName, double threshold, int userId,
                                 String bgImageId, String longitude, String latitude, String appRecordId,String appId) {
         long start = System.currentTimeMillis();
        int parentId=0;
        int thisId;
        Double doubleStrSimilarity;
        // 如果三方库有查询结果
        if (!CollectionUtils.isEmpty(resultList)) {
            JSONObject job = resultList.getJSONObject(0);
            doubleStrSimilarity = job.getDoubleValue("similarity");
            if (doubleStrSimilarity >= threshold) { // 当相似度大于等于系统配置的相似度
                // 当相似度大于等于系统配置的相似度
                Long personId = job.getLongValue("personId");
                log.info("==>cooperationRecord 1 personId:{}",personId);
                Person p = personMapper.selectPersonValidByIdAndAppId(personId,appId);

                // 聚合逻辑
                // 查询当前记录人创建的上一条数据
                List<FaceRecord> frList;
                frList = faceRecordMapper.getNewListByCreateUser((long) userId);
                thisId = recordToGroup(frList, userId, doubleStrSimilarity, pathName, p, personId ,
                        bgImageId, longitude, latitude, appRecordId,appId);
            } else {// 有识别结果但是相似度小于数据库配置的情况下只记录一条face_record人脸识别数据
                parentId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, null, 0 ,
                        bgImageId, longitude, latitude, appRecordId,appId); // 保存此次记录
                log.info("==>cooperationRecord 1 parentId:{}",parentId);
                thisId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, null, parentId ,
                         bgImageId, longitude, latitude, appRecordId,appId); // 保存子集
            }
        } else {
            parentId = recordFunction.saveFaceRecord(userId, 0D, pathName, null, 0 ,  bgImageId, longitude,
                    latitude, appRecordId,appId); // 保存此次记录
            thisId = parentId;
        }
         long end = System.currentTimeMillis();
        log.info("cooperationRecord" + (end - start));
        return thisId;
    }

    //聚合业务
    public int recordToGroup(List<FaceRecord> frList, int userId, Double doubleStrSimilarity, String pathName, Person p,
                             Long personId,String bgImageId, String longitude, String latitude, String appRecordId,String appId) {
        int thisId;
        int parentId;
        if (!CollectionUtils.isEmpty(frList)) {
            FaceRecord newFr = frList.get(0);
            log.info("******************************" + newFr.getId());
            // 查询上一个人是否是此次查询的人
            if (newFr.getPersonId().intValue() == personId.intValue()) {// 如果是则加入上次的聚合
                log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + newFr.getPersonId().intValue() + "$$$$$$$" + personId.intValue());
                FaceRecord parentFr;
                Long st = System.currentTimeMillis();
                // 查看这条数据与现在是否超过5分钟 ,如果超过5分钟则不加入聚合，开启新的数据记录
                log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + newFr.getParentId());
                if (newFr.getParentId() == 0) {
                    parentFr = newFr;
                } else {
                    parentFr = faceRecordMapper.getByPrimaryKey(newFr.getParentId());
                }
                Long en = parentFr.getCreateTime().getTime();
                if (st - en < 300000) {
                    parentFr.setLastRecordTime(new Date());
                    if (parentFr.getSimilarity() < doubleStrSimilarity) {
                        parentFr.setSimilarity(doubleStrSimilarity); // 更新父数据的相似度为此次查询的最高相似度
                    }
                    // 更新父级的最近识别时间为这次识别的时间
                    parentFr.setPic(pathName);
                    faceRecordMapper.updateByPrimaryKeySelective(parentFr); // 更新父数据
                    thisId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, p, parentFr.getId()  ,bgImageId, longitude, latitude, appRecordId,appId); // 保存此次记录
                    // 保存子集的相似结果
                } else {
                    parentId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, p, 0  ,bgImageId, longitude, latitude, appRecordId,appId);
                    thisId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, p, parentId  , bgImageId,longitude, latitude, appRecordId,appId);
                }
            } else { // 如果不是上一次查询的person，则不参与聚合，创建独立的一条数据
                parentId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, p, 0  ,bgImageId, longitude, latitude, appRecordId,appId);
                thisId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, p, parentId  ,bgImageId, longitude, latitude, appRecordId,appId);
            }
        } else {
            // 如果不是，不参与聚合，创建独立的一条数据
            parentId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, p, 0  ,bgImageId, longitude, latitude, appRecordId,appId); // 保存此次记录
            thisId = recordFunction.saveFaceRecord(userId, doubleStrSimilarity, pathName, p, parentId  , bgImageId,longitude, latitude, appRecordId,appId); // 保存子集
            // 保存子集的相似结果
        }
        return thisId;
    }

    public void createLikeList(JSONArray resultList,String appId) {
        JSONObject ob;
        Person p;
        for (int i = 0; i < resultList.size(); i++) {
            ob = resultList.getJSONObject(i);
            Long faceId = ob.getLongValue("personId");
            log.info("--->faceId:{}",faceId);
            p = personMapper.selectPersonValidByIdAndAppId(faceId,appId);
            ob.put("personId",faceId);
            ob.remove("faceId");
            if (p != null) {
                ob.put("personName",p.getName());
                ob.put("personCard",p.getCard());
                ob.put("sex",p.getSex());
                ob.put("isWarning",p.getIsWarning());
                ob.put("address",p.getAddress()==null?"":p.getAddress());
                ob.put("pic", StringUtils.isNotEmpty(p.getPic())? pic_url +p.getPic():"");
                ob.put("birthday",p.getBirthday()==null?"":p.getBirthday());
            }else{
                ob.put("personName","");
                ob.put("personCard","");
                ob.put("sex",2);
                ob.put("isWarning",0);
                ob.put("address","");
                ob.put("birthday","");
                ob.put("pic","");
            }
            resultList.remove(i);
            resultList.add(i,ob);
        }
    }

    public Map<String, Object> createRecordData(int faceRecordId, HttpServletRequest request, int congfigSimilarity,String appId) {
        Map<String, Object> record = new HashMap<>();
        FaceRecord fr = faceRecordMapper.selectByPrimaryKey(faceRecordId);
        RequestContext requestContext = new RequestContext(request);
        if (fr != null) {
            record.put("id", fr.getParentId() == 0 ? fr.getId() : fr.getParentId());
            record.put("isWarning", fr.getIsWarning());
            record.put("pic", pic_url + fr.getPic());
            record.put("comparePic", pic_url + fr.getComparePic());
            record.put("similarity", fr.getSimilarity());
            if (fr.getSimilarity() >= congfigSimilarity) {
                Person p = personMapper.selectPersonValidByIdAndAppId(fr.getPersonId(),appId);
                int sex = 2;
                if (p != null) {
                    sex = p.getSex();
                }
                record.put("sex",sex);
                record.put("personName", fr.getPersonName());
                record.put("personCard", fr.getPersonCard());
                record.put("personId", fr.getPersonId());
                record.put("isInLib", 1);
            } else {
                record.put("sex", 2);
                record.put("personId", 0);
                record.put("isInLib", 0);
                record.put("personName", requestContext.getMessage("wz"));
                record.put("personCard", requestContext.getMessage("bzkz"));
            }
            String warningData = fr.getWarningType();
//            if (!StringUtils.isEmpty(warningData)) {
//                List<Integer> warningList = WarningUtils.getWarningList(warningData);
//                for (int i = 0; i < warningList.size(); i++) {
//                    if (warningList.get(i) == 2) {
//                        warningList.remove(i);
//                    }
//                }
//                record.put("face_warning", warningList);
//            } else {
//                record.put("face_warning", null);
//            }
//            record.put("nation", "");
            if (!StringUtils.isEmpty(fr.getCreateTime())) {
                record.put("createTime", DateUtils.dateTimeToString(fr.getCreateTime()));
            } else {
                record.put("createTime", "");
            }
        }
        return record;
    }

    /**
     * 去重
     * @param userId
     * @param sourcePicBase64
     * @param landmarkJSON
     * @param resultList
     * @param pathName
     * @return
     */
    public JSONArray compareToDuplicateRemovel(Integer userId, String sourcePicBase64, String landmarkJSON, JSONArray resultList, String pathName) {
        if (duplicateRemovalFlag) {
            String imageName = pathName.replace(Constants.FACE_RECORD_PIC_URL + "/", "");
            String duplicateRemoval = duplicateRemovalService.duplicateRemoval(userId, imageName,
                    sourcePicBase64, landmarkJSON);
            if(StringUtils.isNotEmpty(duplicateRemoval)){
                JSONObject duplicateRemovalJSON = JSONObject.parseObject(duplicateRemoval);
                if (duplicateRemovalJSON.getBooleanValue("isExist")) {
                    String resultImageName = duplicateRemovalJSON.getString("name");
                    List<Map<String, Object>> faceLike = faceRecordMapper.getResultList(Constants.FACE_RECORD_PIC_URL  + "/"+resultImageName);
                    log.info("faceLike" + JSON.toJSONString(faceLike));
                    if (CollectionUtils.isEmpty(faceLike)) {
                        resultList = null;
                    } else {
                        resultList = JSONArray.parseArray(JSON.toJSONString(faceLike));
                    }
                } else {
                    resultList = null;
                }
            }
        } else {
            resultList = null;
        }
        return resultList;
    }
}
