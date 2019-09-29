package com.llvision.face.service;

import javax.servlet.http.HttpServletRequest;

import com.llvision.face.vo.Result;

/**
 * @Author: wangwt
 * @Date: 2019/5/20 15:03
 * @Version 1.0
 */
public interface IWorkRecordService {

	/**
	 * 人像识别
	 * 
	 * @param userId
	 * @param appId
	 * @param results
	 * @param sourcePicBase64
	 * @param request
	 * @return
	 */
	Result faceRecord(Integer userId, String appId, String results, String sourcePicBase64,String landmarkJSON,String imageId,HttpServletRequest request, String appRecordId);

	/**
	 * 人像识别记录--分页
	 * @param userId
	 * @param appId
	 * @param pageNum
	 * @param pageSize
	 * @param request
	 * @return
	 */
	Result faceRecordPagingList(int userId, String appId, Integer pageNum, Integer pageSize, HttpServletRequest request);

	/**
	 * 人脸识别详情
	 * @param userId
	 * @param appId
	 * @param recordId
	 * @param request
	 * @return
	 */
	Result faceRecordInfo(int userId, String appId, Integer recordId, HttpServletRequest request,String appRecordId);
}
