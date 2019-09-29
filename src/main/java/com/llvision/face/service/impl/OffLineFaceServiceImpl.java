package com.llvision.face.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.llvision.face.dao.mapper.SystemConfMapper;
import com.llvision.face.service.WatchDogService;
import com.llvision.face.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.constants.Constants;
import com.llvision.face.dao.biz.OfflineFaceBiz;
import com.llvision.face.dao.biz.PersonBiz;
import com.llvision.face.dao.biz.SystemBiz;
import com.llvision.face.dao.biz.WareHouseFaceBiz;
import com.llvision.face.dao.mapper.OffLineFaceMapper;
import com.llvision.face.entity.OffLineFace;
import com.llvision.face.entity.Person;
import com.llvision.face.entity.SystemConf;
import com.llvision.face.entity.WareHouseFace;
import com.llvision.face.response.PersonPojo;
import com.llvision.face.service.IOffLineFaceService;
import com.llvision.face.utils.DateUtils;
import com.llvision.face.utils.PagedModelList;
import com.llvision.face.utils.StringUtils;
import com.llvision.face.vo.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:49
 * @Version 1.0
 */
@Service
@Transactional
@Slf4j
public class OffLineFaceServiceImpl implements IOffLineFaceService {

    @Resource
    private OffLineFaceMapper offLineFaceMapper;

    @Resource
    private PersonBiz personBiz;

	@Resource
	private Llvision00052Service llvision00052Service;

//    @Value("${visit_url}")
//    private String visit_url;

    @Value("${flag}")
    private String flag;

    @Value("${featureExtractUrl}")
    private String featureExtractUrl;

    @Resource
    private WareHouseFaceBiz wareHouseFaceBiz;

    @Resource
    private OfflineFaceBiz offlineFaceBiz;

    @Resource
    private SystemBiz systemBiz;

    @Value("${pic_url}")
    private String pic_url;

    @Resource
    private SystemConfMapper systemConfMapper;

    @Resource
    private WatchDogService watchDogService;

    @Override
    public Result addWarehouse(Integer userId, String name, Integer status, String appId, Integer type, HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();

        if (offlineFaceBiz.checkDelByUserIdAndName(name, userId, type,appId)) { //检验当前用户 库name有没用重名
            r.setMsg(requestContext.getMessage("kmycz"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        if (!StringUtils.regexStr(name)) {
            r.setMsg(requestContext.getMessage("qwsstszf"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        OffLineFace offlineFace = new OffLineFace();
        offlineFace.setCreateUser(userId);
        offlineFace.setName(name);
        offlineFace.setStatus(status == null ? 0 : status);
        offlineFace.setUpdateUser(userId);
        offlineFace.setUpdateTime(new Date());
        offlineFace.setCreateTime(new Date());
        offlineFace.setType(type);
        offlineFace.setIsDel(0);
        offlineFace.setAppId(appId);
        offLineFaceMapper.insert(offlineFace);
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        dateMap.put("userId", userId);
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result deleteWarehouse(Integer userId, Integer libId, String appId,HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();

        OffLineFace offlineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offlineFace == null ) {
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        offlineFaceBiz.del(libId,appId);  //删除此库
        List<WareHouseFace> list = wareHouseFaceBiz.getListByWarehouseId(libId);
        for (WareHouseFace a : list) {
			if (Constants.CUSTOMER.equals("llvision00052")) {
				llvision00052Service.deleteFeature(a.getPersonId());
			}
            personBiz.del(a.getPersonId(),appId);//删除此库里面的人像
        }
        wareHouseFaceBiz.delByLibId(Long.valueOf(libId)); //删除此库和人像的关联表
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        dateMap.put("userId", userId);
        r.setData(dateMap);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result warehouseInfo(Integer libId, String appId,HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();
        OffLineFace offLineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offLineFace == null) { // 如果实普通管理员无法访问该列表
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        if (offLineFace != null) {
            dateMap.put("id", offLineFace.getId());
            dateMap.put("name", offLineFace.getName());
            dateMap.put("status", offLineFace.getStatus());
            dateMap.put("create_user", offLineFace.getCreateUser());
            dateMap.put("update_user", offLineFace.getUpdateUser());
            dateMap.put("create_time", DateUtils.dateTimeToString(offLineFace.getCreateTime()));
            dateMap.put("update_time", DateUtils.dateTimeToString(offLineFace.getUpdateTime()));
        }
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result editWarehouse(Integer libId, Integer userId, String name, Integer status, String appId,
                                Integer type,HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();

        OffLineFace offlineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offlineFace == null ) { // 如果实普通管理员无法访问该列表
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        if (!offlineFace.getName().equals(name) && offlineFaceBiz.checkDelByUserIdAndName(name, userId, type,appId)) { //检验当前用户 库name有没用重名
            r.setMsg(requestContext.getMessage("kmycz"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        if (!StringUtils.regexStr(name)) {
            r.setMsg(requestContext.getMessage("qwsstszf"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        if (offlineFace != null) {
            offlineFace.setName(name);
            offlineFace.setStatus(status == null ? 0 : status);
            offlineFace.setUpdateUser(userId);
            offlineFace.setUpdateTime(new Date());
            offLineFaceMapper.updateByPrimaryKeySelective(offlineFace);
        }
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        dateMap.put("userId", userId);
        r.setData(dateMap);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result faceList(Integer libId, String appId, Integer pageNum, Integer pageSize,HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();
        OffLineFace offlineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offlineFace == null) {
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        PagedModelList<Person> pList = personBiz.getPageList(libId, pageNum, pageSize);//获取分页列表
        List<PersonPojo> personPojoList = new ArrayList<>();
        Map<String, Object> dateMap = new HashMap<>();//返回给客户端
        for (Person a : pList.getPagedModelList()) {
            PersonPojo personPojo = new PersonPojo();
            personPojo.setId(a.getId());

            personPojo.setPic(pic_url + a.getPic());
            /*if (!Constants.CUSTOMER.equals("pudong")) {
            } else {
                personPojo.setPic(visit_url + "/" + a.getPic());
            }*/
            personPojo.setName(a.getName());
            personPojo.setCard(a.getCard());
            personPojo.setSex(a.getSex());
            personPojo.setBirthday(a.getBirthday());
            personPojo.setAddress(a.getAddress());
            personPojo.setStatus(a.getIsWarning());
            personPojo.setModel(a.getModel());
            personPojo.setNation(a.getNation());
            personPojo.setCreate_time(DateUtils.dateTimeToString(a.getCreateTime()));
            personPojo.setUpdate_time(DateUtils.dateTimeToString(a.getUpdateTime()));
            personPojo.setTime1(DateUtils.dateToDateStr(a.getCreateTime()));
            personPojo.setTime2(DateUtils.dateToTimeStr(a.getCreateTime()));
            personPojo.setUpdate_time1(DateUtils.dateToDateStr(a.getUpdateTime()));
            personPojo.setUpdate_time2(DateUtils.dateToTimeStr(a.getUpdateTime()));
            personPojoList.add(personPojo);
        }
        dateMap.put("faceList", personPojoList);
        dateMap.put("pageTotal", pList.getPageCount());
        dateMap.put("pageNum", pageNum);
        dateMap.put("total", pList.getTotalCount());
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        r.setData(dateMap);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result initialization(Integer userId, String appId, HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();
        SystemConf config = new SystemConf();
        config.setAppId(appId);
        SystemConf systemConf = systemBiz.getSystemConf(config);
        if (systemConf == null) {
            config.setAppId("default");
            systemConf = systemBiz.getSystemConf(config);
            SystemConf insertConfig = new SystemConf();
            insertConfig.setAppId(appId).setAngle(systemConf.getAngle()).setSharpness(systemConf.getSharpness())
                    .setSimilarity(systemConf.getSimilarity()).setApiHost(systemConf.getApiHost()).setMqttHost(systemConf.getMqttHost())
                    .setAppSecret(systemConf.getAppSecret()).setCollectMode(systemConf.getCollectMode()).setCompareMode(systemConf.getCompareMode());
            systemConfMapper.insertSelective(insertConfig);
        }
        Map<String, Object> dataMap = new HashMap<>();//返回给客户端
        Map<String, Object> systemConfData = new HashMap<>();//返回给客户端
        int compareMode = (int)watchDogService.decryptFile("compareMode");
        if(null!=systemConf){
            systemConfData.put("sharpness",systemConf.getSharpness());
            systemConfData.put("angle",systemConf.getAngle());
            systemConfData.put("compareMode",compareMode);
            systemConfData.put("similarity",systemConf.getSimilarity());
			systemConfData.put("collectMode", systemConf.getCollectMode());
        }
        List<Map<String,Object>> listByUserId = new ArrayList<>();
        if(compareMode==1){
            listByUserId = personBiz.getPersonListByUserId(userId,appId);
            if(!CollectionUtils.isEmpty(listByUserId)){
                for (int i =0;i<listByUserId.size();i++){
                    Map<String,Object> ele= listByUserId.get(i);
                    String pic = ele.get("pic").toString();
                    ele.put("pic",pic_url+pic);
                    listByUserId.remove(i);
                    listByUserId.add(i,ele);
                }
            }
        }
        dataMap.put("list",listByUserId);
        dataMap.put("systemConfData",systemConfData);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        r.setData(dataMap);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }



    private Long offLineFacePersonCount(String appId) {
        return offLineFaceMapper.offLineFacePersonCount(appId);
    }

    @Override
    public boolean checkFaceCount(String appId,long offset) {
        // 没配置的不做限制
        boolean flag = true;
        Long faceCountNum = offLineFacePersonCount(appId);
        faceCountNum = faceCountNum == null ? offset : faceCountNum + offset;

        String appLevel = "";

        JSONObject decryptJson = watchDogService.decryptFile();
        String level1 = decryptJson.getString("level1") + "";
        String level2 = decryptJson.getString("level2") + "";
        String level3 = decryptJson.getString("level3") + "";

        if (level1.contains(appId)) {
            appLevel = "level1";
        } else if (level2.contains(appId)) {
            appLevel = "level2";
        } else if (level3.contains(appId)) {
            appLevel = "level3";
        }
        // 配置不存在不做限制
        if ("".equals(appLevel)) {
            log.info("-----appid:{} level:{}  levelNum:{} flag:{}",appId,appLevel,faceCountNum,flag);
            return true;
        }

        Long levelNum = decryptJson.getLongValue(appLevel + "Num");

        levelNum = levelNum == null ? 0 : levelNum;

        if (faceCountNum >= levelNum.intValue()) {
            flag = false;
        }
        log.info("-----appid:{} level:{}  levelNum:{} flag:{}",appId,appLevel,levelNum,flag);
        return flag;
    }

    @Override
    public List<Integer> getAvailableLibIds(Integer userId, String appId, int type) {
        return offlineFaceBiz.getAvailableLibIds(userId,appId,type);
    }
}
