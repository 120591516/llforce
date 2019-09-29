package com.llvision.face.service;

import com.llvision.face.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: guoyc
 * @Date: 2019/5/21 10:11
 * @Version 1.0
 */
public interface IWarehouseUserService {
    /**
     * 添加人库关联
     * @param userId
     * @param libId
     * @param request
     * @return
     */
    Result addWarehouseUser(Integer userId, Integer libId,String appId, HttpServletRequest request);

    /**
     * 删除人库关联
     * @param userId
     * @param libId
     * @param request
     * @return
     */
    Result delWarehouseUser(Integer userId, Integer libId,String appId, HttpServletRequest request);


    /**
     * 获取人库关联
     * @param userId
     * @param request
     * @return
     */
    Result warehouseUserList(Integer userId,String appId, HttpServletRequest request);
}
