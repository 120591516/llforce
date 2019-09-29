package com.llvision.face.service;

import com.llvision.face.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:49
 * @Version 1.0
 */
public interface IOffLineFaceService {
    /**
     * 添加离线库
     * @param userId
     * @param name
     * @param status
     * @param appId
     * @param type
     * @return
     */
    Result addWarehouse(Integer userId, String name, Integer status, String appId, Integer type, HttpServletRequest request);


    /**
     * 删除离线库
     * @param userId
     * @param libId
     * @param appId
     * @return
     */
    Result deleteWarehouse(Integer userId,Integer libId, String appId,HttpServletRequest request);


    /**
     * 离线库详情
     * @param libId
     * @param appId
     * @return
     */
    Result warehouseInfo(Integer libId,String appId,HttpServletRequest request);

    /**
     * 编辑离线库
     * @param libId
     * @param userId
     * @param name
     * @param status
     * @param appId
     * @param type
     * @return
     */
    Result editWarehouse(Integer libId,Integer userId,String name,Integer status,String appId,Integer type,HttpServletRequest request);

    /**
     * 离线库人像列表
     * @param libId
     * @param appId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result faceList(Integer libId,String appId,Integer pageNum,Integer pageSize,HttpServletRequest request);

    /**
     * APP端初始化
     * @param userId
     * @param appId
     * @param request
     * @return
     */
    Result initialization(Integer userId, String appId, HttpServletRequest request);


    boolean checkFaceCount(String appId,long offset);

    List<Integer> getAvailableLibIds(Integer userId, String appId, int type);
}
