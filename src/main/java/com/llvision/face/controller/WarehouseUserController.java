package com.llvision.face.controller;

import com.llvision.face.service.IWarehouseUserService;
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
 * @Date: 2019/5/21 12:08
 * @Version 1.0
 */
@RestController
@Api(description = "人库操作",tags = {"人库操作"})
@Slf4j
@RequestMapping("/web/v1/offline")
public class WarehouseUserController {

    @Resource
    private IWarehouseUserService warehouseUserService;
    @ApiOperation(value="添加人库关联",response = Result.class,notes="添加人库关联")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "libId",value = "库id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    @PutMapping("addWarehouseUser")
    public Result addWarehouseUser(@RequestParam(value = "userId") Integer userId,
                                   @RequestParam(value = "libId") Integer libId,
                                   @RequestParam(value = "appId") String appId,
                                   HttpServletRequest request) {

        return warehouseUserService.addWarehouseUser(userId,libId,appId,request);
    }


    @ApiOperation(value="删除人库关联",response = Result.class,notes="删除人库关联")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "libId",value = "库id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    @DeleteMapping("delWarehouseUser")
    public Result delWarehouseUser(@RequestParam(value = "userId") Integer userId,
                                   @RequestParam(value = "libId") Integer libId,
                                   @RequestParam(value = "appId") String appId,
                                   HttpServletRequest request) {

        return warehouseUserService.delWarehouseUser(userId,libId,appId,request);
    }

    @ApiOperation(value="获取人库关联",response = Result.class,notes="获取人库关联")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "int"),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String")
    })
    @GetMapping("warehouseUserList")
    public Result warehouseUserList(@RequestParam(value = "userId") Integer userId,
                                    @RequestParam(value = "appId") String appId,
                                   HttpServletRequest request) {

        return warehouseUserService.warehouseUserList(userId,appId,request);
    }
}
