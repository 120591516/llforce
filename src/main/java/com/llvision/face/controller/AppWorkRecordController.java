package com.llvision.face.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.llvision.face.service.IWorkRecordService;
import com.llvision.face.vo.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: wangwt
 * @Date: 2019/5/20 14:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/app/v1/workRecord")
@Slf4j
@Api(description = "APP工作记录模块", tags = { "APP工作记录模块" })
public class AppWorkRecordController {
	@Resource
	private IWorkRecordService workRecordService;
	 /**
     * 人脸识别
     *
     * @param userId          用户id
     * @param results         眼镜识别结果，数据格式为map类型的json 例如：[{personId:1,similarity:90},{personId:2,similarity:80}]
     * @param sourcePicBase64 识别图片的base64格式数据
     * @param appId
     * compareMode	  识别模式，0、离线 1、在线
     * @author shihy
     */
    @ResponseBody
	@PostMapping("/faceRecord")
    @ApiOperation(value="人脸识别",response = Result.class,notes="人脸识别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "results",value = "眼镜识别结果，数据格式为map类型的json 例如：[{personId:1,similarity:90},{personId:2,similarity:80}]",required = false,dataType = "String",defaultValue = ""),
            @ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
			@ApiImplicitParam(name = "sourcePicBase64", value = "识别图片的base64格式数据", required = true, dataType = "String"),
			@ApiImplicitParam(name = "landmarkJSON", value = "landmark比对JSON", required = true, dataType = "String"),
			@ApiImplicitParam(name = "imageId", value = "识别照片id", required = false, dataType = "String"),
			@ApiImplicitParam(name = "appRecordId", value = "app记录Id",required = false,dataType = "String")
    })
    public Result faceRecord(@RequestParam(value = "userId") Integer userId,
    						 @RequestParam(value = "appId") String appId,
                             @RequestParam(value = "results", required = false, defaultValue = "") String results,
                             @RequestParam(value = "sourcePicBase64", required = false, defaultValue = "") String sourcePicBase64,
                             @RequestParam(value = "landmarkJSON", required = true, defaultValue = "") String landmarkJSON,
                             @RequestParam(value = "imageId", required = false, defaultValue = "") String imageId,
							 @RequestParam(value = "appRecordId",required = false) String appRecordId,
                             HttpServletRequest request) throws Exception {
		Result r = workRecordService.faceRecord(userId, appId, results, sourcePicBase64,landmarkJSON,imageId, request, appRecordId);
        return r;
    }

	/**
	 *识别记录--分页
	 * @param userId
	 * @param appId
	 * @param pageNum
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/faceRecordPagingList")
	@ApiOperation(value="识别记录--分页",response = Result.class,notes="识别记录--分页")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "int"),
			@ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
			@ApiImplicitParam(name = "pageNum", value = "页数",  dataType = "int"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示数量", dataType = "int"),
	})
	public Result faceRecordPagingList(@RequestParam(value = "userId") int userId,
									   @RequestParam(value = "appId") String appId,
									   @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
									   @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
									   HttpServletRequest request) {
		Result r = workRecordService.faceRecordPagingList(userId, appId, pageNum, pageSize, request);
		return r;
	}

	/**
	 * 人脸识别详情
	 *
	 * @param userId   当前用户Id
	 * @param appId    用户token
	 * @param recordId 数据库id
	 * @author wang
	 */
	@ResponseBody
	@GetMapping("/faceRecordInfo")
	@ApiOperation(value="识别记录--详情",response = Result.class,notes="识别记录--详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId",value = "操作人uid",required = true,dataType = "Integer"),
			@ApiImplicitParam(name = "appId",value = "应用id",required = true,dataType = "String"),
			@ApiImplicitParam(name = "recordId", value = "识别记录id",required = false,dataType = "Integer"),
			@ApiImplicitParam(name = "appRecordId", value = "app记录id",required = false,dataType = "String")
	})
	public Result faceRecordInfo(@RequestParam(value = "userId") int userId,
								 @RequestParam(value = "appId") String appId,
								 @RequestParam(value = "recordId",required = false) Integer recordId,
								 @RequestParam(value = "appRecordId",required = false) String appRecordId,
								 HttpServletRequest request) {
		Result r = workRecordService.faceRecordInfo(userId, appId, recordId, request,appRecordId);
		return r;
	}
}
