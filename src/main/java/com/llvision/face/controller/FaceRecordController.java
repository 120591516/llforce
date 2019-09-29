package com.llvision.face.controller;

import com.llvision.face.service.IFaceRecordService;
import com.llvision.face.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: guoyc
 * @Date: 2019/5/21 10:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/web/v1/offline")
@Slf4j
@Api(description = "识别记录",tags = {"识别记录"})
public class FaceRecordController {

    @Resource
    private IFaceRecordService faceRecordService;

    @ApiOperation(value="识别记录",response = Result.class,notes="识别记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = false,dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量",required = false,dataType = "int",defaultValue = "20"),
            @ApiImplicitParam(name = "pageNum",value = "页码",required = false,dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    @GetMapping("/faceRecord")
    public Result faceRecordList(@RequestParam(value = "userId",required = false) Integer userId,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize,
                                 @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "appId") String appId,
                                 HttpServletRequest request) {
        return faceRecordService.faceRecordList(userId,pageSize,pageNum,appId,request);
    }
}
