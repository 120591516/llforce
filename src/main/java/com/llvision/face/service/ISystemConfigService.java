package com.llvision.face.service;

import com.llvision.face.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: guoyc
 * @Date: 2019/5/17 16:09
 * @Version 1.0
 */
public interface ISystemConfigService {

    /**
     * 获取配置信息
     * @return
     */
    Result getSystemConfig(String appId,HttpServletRequest request);

    /**
     * 配置修改
     * @param appId
     * @param appSecret
     * @param apiHost
     * @param mqttHost
     * @param similarity
     * @param angle
     * @param configId
     * @param sharpness
     * @return
     */
    Result updateSystemConfig(String appId, String appSecret, String apiHost, String mqttHost, Integer similarity, Integer angle, Integer configId, Integer sharpness, HttpServletRequest request);
}
