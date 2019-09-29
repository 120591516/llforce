package com.llvision.face.service;

import com.llvision.face.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:53
 * @Version 1.0
 */
public interface IWareHouseFaceService {
    /**
     * 人像库列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param type
     * @param appId
     * @return
     */
    Result warehouseList(Integer userId, Integer pageNum, Integer pageSize, Integer type, String appId, HttpServletRequest request);

    /**
     *  批量上传人像（支持jpg\JPG|png|PNG）
     * @param userId
     * @param type
     * @param libId
     * @param appId
     * @param files
     * @return
     */
    Result uploadFacePic(Integer userId, Integer type, Integer libId, String appId, MultipartFile[] files,HttpServletRequest request);

    /**
     * 批量上传人像Excel(xlsx|xls)
     * @param userId
     * @param file
     * @param libId
     * @param appId
     * @return
     */
    Result uploadExcel(Integer userId,MultipartFile file,Integer libId,String appId,HttpServletRequest request) throws IOException;
}
