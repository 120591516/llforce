package com.llvision.face.controller;

import com.alibaba.fastjson.JSONObject;
import com.llvision.face.service.IOffLineFaceService;
import com.llvision.face.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/web/v1/offline")
@Slf4j
@Api(description = "离线人像库管理",tags = {"离线人像库管理"})
public class OffLineFaceController {

    @Resource
    private IOffLineFaceService offLineFaceService;

    /**
     * 添加离线人像库
     *
     * @param userId 当前用户Id
     * @param name   库名
     * @param status 是否激活  0：不激 1：激活
     * @author wangag
     */
    @ResponseBody
    @PutMapping("/addWarehouse")
    @ApiOperation(value="添加离线人像库",response = Result.class,notes="用户添加离线人像库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "name",value = "离线库名",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "status",value = "0未激活 1激活",required = true,dataType = "int"),
            @ApiImplicitParam(name = "type",value = "1:  人像   2：车牌(预留)",required = false,dataType = "int",defaultValue = "1"),
    })
    public Result addWarehouse(@RequestParam(value = "userId") Integer userId,
                               @RequestParam(value = "name", required = false, defaultValue = "") String name,
                               @RequestParam(value = "status") Integer status,
                               @RequestParam(value = "appId") String appId,
                               @RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
                               HttpServletRequest request) {
        Result r = offLineFaceService.addWarehouse(userId,name,status,appId,type,request);
        return r;
    }

    /**
     * 删除离线库 (不是自己创建的库无法删除)
     *
     * @param userId      当前用户Id
     * @param libId 库ID
     * @param appId 应用id
     * @author wangag
     */
    @ResponseBody
    @DeleteMapping("/deleteWarehouse")
    @ApiOperation(value="删除离线库",response = Result.class,notes="不是自己创建的库无法删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "libId",value = "离线库id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    public Result deleteWarehouse(@RequestParam(value = "userId") Integer userId,
                                  @RequestParam(value = "libId") Integer libId,
                                  @RequestParam(value = "appId") String appId,
                                  HttpServletRequest request) {
        Result r = offLineFaceService.deleteWarehouse(userId,libId,appId,request);
        return r;
    }

    /**
     * 获得离线库详情
     *
     * @param libId 离线库id
     * @author wangag
     */
    @ResponseBody
    @GetMapping("/warehouseInfo")
    @ApiOperation(value="获得离线库详情",response = Result.class,notes="通过libId获得离线库详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "libId",value = "离线库id",required = true,dataType = "int")
    })
    public Result warehouseInfo(@RequestParam(value = "libId") Integer libId,
                                @RequestParam(value = "appId") String appId,
                                HttpServletRequest request) {
        Result r = offLineFaceService.warehouseInfo(libId,appId,request);
        return r;
    }


    /**
     * 编辑离线人像库
     *
     * @param libId 离线库Id
     * @param userId      当前用户Id
     * @param name        库名
     * @param status      是否激活  0：不激 1：激活
     * @author wangag
     */
    @ResponseBody
    @PutMapping("/editWarehouse")
    @ApiOperation(value="编辑离线人像库",response = Result.class,notes="通过libId编辑离线人像库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "libId",value = "离线库id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用Id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "name",value = "离线库名",required = false,dataType = "int",defaultValue = ""),
            @ApiImplicitParam(name = "status",value = "是否激活  0：不激 1：激活",required = true,dataType = "int"),
            @ApiImplicitParam(name = "type",value = "1:  人像   2：车牌(预留)",required = false,dataType = "int",defaultValue = "1")
    })
    public Result editWarehouse(@RequestParam(value = "libId") Integer libId,
                                @RequestParam(value = "userId") Integer userId,
                                @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                @RequestParam(value = "status") Integer status,
                                @RequestParam(value = "appId") String appId,
                                @RequestParam(value = "type", required = false, defaultValue = "1") int type,
                                HttpServletRequest request) {

        return offLineFaceService.editWarehouse(libId,userId,name,status,appId,type,request);
    }


    /**
     * 离线库人像列表
     *
     * @param libId 离线库id
     * @param pageNum     页码  未传值默认是第一页
     * @param pageSize    单页数量  未传值默认是20
     * @author wangag
     */
    @ResponseBody
    @GetMapping("/faceList")
    @ApiOperation(value="离线库人像列表",response = Result.class,notes="离线库人像列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "libId",value = "离线库id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "pageNum",value = "页码",required = false,dataType = "int",defaultValue = ""),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量",required = false,dataType = "int",defaultValue = "20")
    })
    public Result faceList(@RequestParam(value = "libId") Integer libId,
                           @RequestParam(value = "appId") String appId,
                           @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                           HttpServletRequest request) {

        return offLineFaceService.faceList(libId,appId,pageNum,pageSize,request);
    }

    @ResponseBody
    @GetMapping("/applevel")
    @ApiOperation(value="applevel",response = Result.class,notes="applevel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    public Result applevel(
                           @RequestParam(value = "appId") String appId,
                           HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();
        boolean ret = offLineFaceService.checkFaceCount(appId,0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ret",ret);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        r.setData(jsonObject);
        return r;
    }

}
