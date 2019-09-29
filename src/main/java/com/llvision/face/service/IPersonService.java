package com.llvision.face.service;

import com.llvision.face.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 15:52
 * @Version 1.0
 */
public interface IPersonService {

    /**
     * 编辑人像信息
     * @param userId
     * @param personId
     * @param name
     * @param card
     * @param sex
     * @param nation
     * @param birthday
     * @param address
     * @param model
     * @param warn
     * @param libId
     * @return
     */
    Result editFace(Integer userId, Long personId, String name, String card, Integer sex, String nation, String birthday,
                    String address, String model, Integer warn, Integer libId,String appId, HttpServletRequest request) throws ParseException;

    /**
     * 添加人像
     * @param userId
     * @param name
     * @param card
     * @param sex
     * @param nation
     * @param birthday
     * @param address
     * @param status
     * @param libId
     * @param file
     * @return
     */
    Result addFace(Integer userId,String name,String card,Integer sex,String nation,String birthday,
                   String address,Integer status,Integer libId,MultipartFile file,String appId,HttpServletRequest request) throws ParseException;


    /**
     * 删除人像
     * @param userId
     * @param personId
     * @return
     */
    Result deleteFace(Integer userId,Long personId,String appId,HttpServletRequest request);

    /**
     * 获取人像信息
     * @param personId
     * @return
     */
    Result getFace(Long personId,String appId,HttpServletRequest request);

}
