package com.llvision.face.controller;

import com.llvision.face.service.IWareHouseFaceService;
import com.llvision.face.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:56
 * @Version 1.0
 */
@RestController
@Api(description = "人像库管理",tags = {"人像库管理"})
@Slf4j
@RequestMapping("/web/v1/offline")
public class WareHouseFaceController {

    @Resource
    private IWareHouseFaceService wareHouseFaceService;

    @GetMapping("/warehouseList")
    @ApiOperation(value="离线库列表",response = Result.class,notes="根据条件查询离线库列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = false,dataType = "int",defaultValue = "0"),
            @ApiImplicitParam(name = "pageNum",value = "页码",required = false,dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "请求数量",required = false,dataType = "int",defaultValue = "20"),
            @ApiImplicitParam(name = "type",value = "1:  人像   2：车牌(预留)",required = false,dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    public Result warehouseList(@RequestParam(value = "userId",required = false,defaultValue = "0") Integer userId,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                @RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
                                @RequestParam(value = "appId") String appId,
                                HttpServletRequest request) {
        return wareHouseFaceService.warehouseList(userId,pageNum,pageSize,type,appId,request);
    }


    @ResponseBody
    @PostMapping(value = "/uploadFacePic",headers = "content-type=multipart/form-data")
    @ApiOperation(value="更新人像图片",response = Result.class,notes="更新人像图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "type",value = "1:  人像   2：车牌(预留)",required = true,dataType = "int"),
            @ApiImplicitParam(name = "libId",value = "离线库id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    public Result uploadFacePic(@RequestParam(value = "userId") Integer userId,
                                @RequestParam(value = "type") Integer type,
                                @RequestParam(value = "libId") Integer libId,
                                @RequestParam(value = "appId") String appId,
                                @ApiParam(name = "files",value = "files",required = true) MultipartFile[] files,
                                HttpServletRequest request) {
        return wareHouseFaceService.uploadFacePic(userId,type,libId,appId,files,request);
    }


    /**
     * 上传Excel
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PutMapping(value = "/uploadExcel",headers = "content-type=multipart/form-data")
    @ApiOperation(value="上传Excel",response = Result.class,notes="上传Excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
            @ApiImplicitParam(name = "libId",value = "离线库id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    public Result uploadExcel(@RequestParam(value = "userId") Integer userId,
                              @ApiParam(name = "file",value = "file",required = true) MultipartFile file,
                              @RequestParam(value = "libId") Integer libId,
                              @RequestParam(value = "appId") String appId,
                              HttpServletRequest request) throws IOException {
        return wareHouseFaceService.uploadExcel(userId,file,libId,appId,request);
    }

}
