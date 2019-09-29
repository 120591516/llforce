package com.llvision.face.controller;

import com.llvision.face.service.ISystemConfigService;
import com.llvision.face.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: guoyc
 * @Date: 2019/5/17 11:28
 * @Version 1.0
 */
@RestController
@RequestMapping("/web/v1/system")
@Slf4j
@Api(description = "配置管理",tags = {"配置管理"})
public class SystemConfigController {

    @Resource
    private ISystemConfigService systemConfigService;

    @GetMapping("/config")
    @ApiOperation(value="获取系统配置",response = Result.class,notes="获取系统配置")
    public Result getSystemConfig(@RequestParam(value = "appId") String appId, HttpServletRequest request) {
        return systemConfigService.getSystemConfig(appId,request);
    }

    @PutMapping("/update")
    @ApiOperation(value="更改系统配置",response = Result.class,notes="更改系统配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "appSecret",value = "应用秘钥",required = false,dataType = "String"),
            @ApiImplicitParam(name = "apiHost",value = "api服务器地址",required = false,dataType = "String"),
            @ApiImplicitParam(name = "mqttHost",value = "mqtt地址",required = false,dataType = "String"),
            @ApiImplicitParam(name = "similarity",value = "识别阈值",required = false,dataType = "int"),
            @ApiImplicitParam(name = "angle",value = "角度",required = false,dataType = "int"),
            @ApiImplicitParam(name = "sharpness",value = "清晰度",required = false,dataType = "int"),
            @ApiImplicitParam(name = "configId",value = "配置id",required = true,dataType = "int")
    })
    public Result updateSystemConfig(@RequestParam(value = "appId") String appId,
                                     @RequestParam(value = "appSecret",required = false) String appSecret,
                                     @RequestParam(value = "apiHost",required = false) String apiHost,
                                     @RequestParam(value = "mqttHost",required = false) String mqttHost,
                                     @RequestParam(value = "similarity",required = false) Integer similarity,
                                     @RequestParam(value = "angle",required = false) Integer angle,
                                     @RequestParam(value = "configId") Integer configId,
                                     @RequestParam(value = "sharpness",required = false) Integer sharpness,
                                     HttpServletRequest request) {
        return systemConfigService.updateSystemConfig(appId,appSecret,apiHost,mqttHost,similarity,
                angle,configId,sharpness,request);
    }
}
