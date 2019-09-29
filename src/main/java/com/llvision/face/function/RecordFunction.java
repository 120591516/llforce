package com.llvision.face.function;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.dao.mapper.FaceLikeMapper;
import com.llvision.face.entity.FaceLike;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.llvision.face.dao.mapper.FaceRecordMapper;
import com.llvision.face.dao.mapper.PersonMapper;
import com.llvision.face.entity.FaceRecord;
import com.llvision.face.entity.Person;
import com.llvision.face.utils.StringUtils;

@Repository
public class RecordFunction {
	private Logger log = Logger.getLogger("");
    @Resource
	private FaceRecordMapper faceRecordMapper;

    @Resource
	private FaceLikeMapper faceLikeMapper;

    @Resource
	private PersonMapper personMapper;

	public int saveFaceRecord(int userId, Double doubleStrSimilarity, String pathName, Person p, int parentId,
			String bgImageId, String longitude, String latitude, String appRecordId,String appId) {
		long start = System.currentTimeMillis();
        FaceRecord fr = new FaceRecord();
        Double strSimilarity = 0.0D;
        fr.setParentId(parentId);
        fr.setPic(pathName);
		fr.setBgImageId(bgImageId);
		fr.setAppRecordId(appRecordId);
		fr.setCreateTime(new Date());
		fr.setAppId(appId);
        double d;
        BigDecimal b;
		if (StringUtils.isNotEmpty(longitude) && !"4.9E-324".equals(longitude)) {
			d = Double.parseDouble(longitude);
			b = new BigDecimal(d);
			d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			fr.setLongitude(d);
		}
		if (StringUtils.isNotEmpty(latitude) && !"4.9E-324".equals(latitude)) {
			d = Double.parseDouble(latitude);
			b = new BigDecimal(d);
			d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			fr.setLatitude(d);
		}
        if (p != null) {
            fr.setPersonId(p.getId());
			fr.setComparePic(p.getPic());
            fr.setPersonCard(p.getCard());
            fr.setPersonName(p.getName());
            fr.setWarningType(p.getWarningType());
            fr.setIsWarning(p.getIsWarning());
            strSimilarity = doubleStrSimilarity;
		}
        fr.setSimilarity(strSimilarity);
        fr.setCreateUser(userId);
		// fr.setUpdateTime(new Date());
        fr.setLastRecordTime(new Date());// 最近识别时间
		faceRecordMapper.insertSelective(fr); // 保存记录
		long end = System.currentTimeMillis();
		log.info("saveFaceRecord" + (end - start));
        return fr.getId();
    }

    public void saveLikeRecord(JSONArray resultList, int frId,String appId) {
		log.info("保存相似结果>>>>");
		log.info("resultList" + resultList.toJSONString());
        // 添加识别结果到face_like库
        FaceLike faceLike;
        Person p;
        JSONObject ob;
        for (int i = 0; i < resultList.size(); i++) {
            faceLike = new FaceLike();
            ob = resultList.getJSONObject(i);

            p = personMapper.selectPersonValidByIdAndAppId(ob.getLongValue("personId"),appId);
            if (p != null) {
                faceLike.setPersonId(p.getId());
                faceLike.setPersonInfo(p.getName() + " " + p.getCard());
				faceLike.setComparePic(p.getPic());
                faceLike.setWarningType(p.getWarningType());
                faceLike.setStatus(p.getIsWarning());
            }
            faceLike.setSimilarity(ob.getDoubleValue("similarity"));
            faceLike.setFaceRecoedId(frId);
            faceLikeMapper.insertSelective(faceLike);
        }
    }

    /**
     * 保存三方库中查询到的人员信息
     *
     * @throws Exception
     */
//    public Map<String, Object> savePersonFromOtherLib(Integer userId, Map<String, Object> dataMap, Person person, HttpServletRequest request) throws Exception {
//        // 保存人员信息
//        String customer = Constants.CUSTOMER;
//		if (!customer.equals("standard") && !customer.equals("dongjian") && !customer.equals("yuetai")
//				&& !customer.equals("huaweicloud")) {
//            if (person != null) {
//                if (!StringUtils.isEmpty(person.getPic())) {
//					if (!person.getPic().contains("http") && !person.getPic().contains("/upload")) {
//						String imagePath = cooperationService.saveImageFromBase64(person.getPic(), request);
//						person.setPic(imagePath);
//					} else {
//						String pic = FileUtils.saveImageForUri(person.getPic(), request);
//						person.setPic(pic);
//
//					}
//                }
//            }
//        }
//
//        person.setCreateUser(userId);
//        person.setUpdateUser(userId);
//        person.setUpdateTime(new Date());
//		log.info("person结果：" + JSONObject.toJSONString(person));
//        if (null == person.getId()) {
//            personService.insert(person);
//        } else {
//            personService.update(person);
//        }
//        dataMap.put("faceId", person.getId());
//        return dataMap;
//    }

}

