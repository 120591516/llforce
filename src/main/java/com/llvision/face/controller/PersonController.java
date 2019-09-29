package com.llvision.face.controller;

import com.llvision.face.service.IPersonService;
import com.llvision.face.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/web/v1/offline")
@Slf4j
@Api(description = "人像管理",tags = {"人像管理"})
public class PersonController {


    @Resource
    private IPersonService personService;

    /**
     * 编辑人像
     *
     * @param personId 人像 Id
     * @param userId   当前用户Id
     * @author wangag
     */
    @ResponseBody
    @PostMapping("/editFace")
    @ApiOperation(value="编辑人像",response = Result.class,notes="编辑人像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "personId",value = "人像id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "name",value = "人像姓名",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "card",value = "身份证信息",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "sex",value = "性别0、女，1、男，2未知",required = false,dataType = "int",defaultValue = "2"),
            @ApiImplicitParam(name = "nation",value = "民族",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "birthday",value = "生日",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "address",value = "地址",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "model",value = "识别模型",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "warn",value = "是否警示0不警示 1警示",dataType = "int"),
            @ApiImplicitParam(name = "libId",value = "离线库id",required = true,dataType = "int")
    })
    public Result editFace(@RequestParam(value = "userId") Integer userId,
                           @RequestParam(value = "personId") Long personId,
                           @RequestParam(value = "name", required = false, defaultValue = "") String name,
                           @RequestParam(value = "card", required = false, defaultValue = "") String card,
                           @RequestParam(value = "sex", required = false, defaultValue = "") Integer sex,
                           @RequestParam(value = "nation", required = false, defaultValue = "") String nation,
                           @RequestParam(value = "birthday", required = false, defaultValue = "") String birthday,
                           @RequestParam(value = "address", required = false, defaultValue = "") String address,
                           @RequestParam(value = "model", required = false, defaultValue = "") String model,
                           @RequestParam(value = "appId") String appId,
                           @RequestParam(value = "warn") Integer warn,
                           @RequestParam(value = "libId") Integer libId,
                           HttpServletRequest request) throws ParseException {
        return personService.editFace(userId,personId,name,card,sex,nation,birthday,address,model,warn,libId,appId,request);
    }

    /**
     * 添加人像
     *
     * @param userId   当前用户Id
     * @param file     照片
     * @param name     姓名
     * @param card     身份证号
     * @param sex      性别
     * @param birthday 生日
     * @param address  居住地
     * @param status   激活状态
     * @author wangag
     */
    @ResponseBody
    @PutMapping(value = "/addFace",headers = "content-type=multipart/form-data")
    @ApiOperation(value="添加人像",response = Result.class,notes="添加人像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "name",value = "姓名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "card",value = "身份证号",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "sex",value = "性别0、女，1、男，2未知",required = false,dataType = "int"),
            @ApiImplicitParam(name = "nation",value = "民族",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "birthday",value = "生日 格式如: 2019-05-31",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "address",value = "地址",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "status",value = "是否警示",required = false,dataType = "int",defaultValue = "0"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "libId",value = "离线库id",required = true,dataType = "int")
    })
    public Result addFace(@RequestParam(value = "userId") Integer userId,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "card", required = false, defaultValue = "") String card,
                          @RequestParam(value = "sex", required = false, defaultValue = "") Integer sex,
                          @RequestParam(value = "nation", required = false, defaultValue = "") String nation,
                          @RequestParam(value = "birthday", required = false, defaultValue = "") String birthday,
                          @RequestParam(value = "address", required = false, defaultValue = "") String address,
                          @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                          @RequestParam(value = "appId") String appId,
                          @RequestParam(value = "libId") Integer libId,
                          @ApiParam(name = "file",value = "file",required = true) MultipartFile file,
                          HttpServletRequest request) throws Exception {
        return personService.addFace(userId,name,card,sex,nation,birthday,address,status,libId,file,appId,request);
    }



    /**
     * 删除人像
     *
     * @param userId   当前用户Id
     * @param personId 人像ID
     * @author wangag
     */
    @ResponseBody
    @DeleteMapping("/deleteFace")
    @ApiOperation(value="删除人像",response = Result.class,notes="删除人像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "personId",value = "人像id",required = true,dataType = "Long")
    })
    public Result deleteFace(@RequestParam(value = "userId") Integer userId,
                             @RequestParam(value = "personId") Long personId,
                             @RequestParam(value = "appId") String appId,
                             HttpServletRequest request) {
        return personService.deleteFace(userId,personId,appId,request);
    }

    /**
     * 获得人像详情
     *
     * @param personId 人像ID
     * @author wangag
     */
    @ResponseBody
    @GetMapping("/getFace")
    @ApiOperation(value="获得人像详情",response = Result.class,notes="获得人像详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId",value = "人像id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    public Result getFace(@RequestParam(value = "personId") Long personId,
                          @RequestParam(value = "appId") String appId,
                          HttpServletRequest request) {
        return personService.getFace(personId,appId,request);
    }

}
