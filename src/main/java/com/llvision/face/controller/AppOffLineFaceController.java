package com.llvision.face.controller;

import com.llvision.face.service.IOffLineFaceService;
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
 * @Author: wangwt
 * @Date: 2019/5/21 15:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/app/v1/offline")
@Slf4j
@Api(description = "APP离线人像库管理",tags = {"APP离线人像库管理"})
public class AppOffLineFaceController {

    @Resource
    private IOffLineFaceService offLineFaceService;

    /**
     * 初始化人像，配置
     *
     * @param userId 当前用户Id
     * @author wangwt
     */
    @ResponseBody
    @GetMapping("/initialization")
    @ApiOperation(value="添加离线人像库",response = Result.class,notes="用户添加离线人像库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
    })
    public Result initialization(@RequestParam(value = "userId") Integer userId,
                               @RequestParam(value = "appId") String appId,
                               HttpServletRequest request) {
        Result r = offLineFaceService.initialization(userId,appId,request);
        return r;
    }

}
