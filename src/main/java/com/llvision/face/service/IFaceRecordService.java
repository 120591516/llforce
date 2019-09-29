package com.llvision.face.service;

import com.llvision.face.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: guoyc
 * @Date: 2019/5/21 10:11
 * @Version 1.0
 */
public interface IFaceRecordService {
    /**
     * 识别记录
     * @param userId
     * @param pageSize
     * @param pageNum
     * @param request
     * @return
     */
    Result faceRecordList(Integer userId,Integer pageSize,Integer pageNum, String appId,HttpServletRequest request);
}
