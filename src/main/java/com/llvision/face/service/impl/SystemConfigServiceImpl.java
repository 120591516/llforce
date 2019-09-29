package com.llvision.face.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llvision.face.constants.Constants;
import com.llvision.face.dao.biz.SystemBiz;
import com.llvision.face.entity.SystemConf;
import com.llvision.face.service.ISystemConfigService;
import com.llvision.face.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: guoyc
 * @Date: 2019/5/17 16:10
 * @Version 1.0
 */

@Service
@Transactional
@Slf4j
public class SystemConfigServiceImpl implements ISystemConfigService {

    @Resource
    private SystemBiz systemBiz;

    @Override
    public Result getSystemConfig(String appId,HttpServletRequest request) {
        Result r = new Result();
        RequestContext requestContext = new RequestContext(request);
        SystemConf conf = new SystemConf().setAppId(appId);
        conf = systemBiz.getSystemConf(conf);
        r.setData(conf);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result updateSystemConfig(String appId, String appSecret, String apiHost, String mqttHost, Integer similarity, Integer angle, Integer configId, Integer sharpness, HttpServletRequest request) {
        Result r = new Result();
        RequestContext requestContext = new RequestContext(request);
        SystemConf conf = new SystemConf();
        // 多脸只支持在线识别
        /*if (collectMode != null && collectMode.intValue() == 2) {
            compareMode = 2;
        }*/

        SystemConf existConfig = new SystemConf();
        existConfig.setId(configId);
        existConfig = systemBiz.selectOne(existConfig);
        if (existConfig == null) {
            r.setMsg(requestContext.getMessage("bcz"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        if (!appId.equals( existConfig.getAppId())) {
            r.setMsg(requestContext.getMessage("xxbz"));
            r.setCode(Constants.OTHER_ERROR_CODE_ERRROR);
            return r;
        }
        conf.setId(configId).setAppSecret(appSecret).setApiHost(apiHost).setMqttHost(mqttHost).setSimilarity(similarity).setAngle(angle).setSharpness(sharpness);
        systemBiz.updateSystemConfig(conf);
        r.setData(new JSONObject());
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }
}
