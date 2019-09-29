package com.llvision.face.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.llvision.face.constants.Constants;
import com.llvision.face.dao.biz.OfflineFaceBiz;
import com.llvision.face.dao.mapper.WarehouseUserMapper;
import com.llvision.face.entity.FaceRecord;
import com.llvision.face.entity.OffLineFace;
import com.llvision.face.entity.WarehouseUser;
import com.llvision.face.service.IWarehouseUserService;
import com.llvision.face.utils.PageUtil;
import com.llvision.face.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContext;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: guoyc
 * @Date: 2019/5/21 12:14
 * @Version 1.0
 */
@Service
@Slf4j
@Transactional
public class WarehouseUserServiceImpl implements IWarehouseUserService {

    @Resource
    private WarehouseUserMapper warehouseUserMapper;

    @Resource
    private OfflineFaceBiz offlineFaceBiz;

    @Override
    public Result addWarehouseUser(Integer userId, Integer libId, String appId,HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        // 验证库是否存在
        OffLineFace offLineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offLineFace == null) {
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        Result r = new Result();
        WarehouseUser warehouseUser = new WarehouseUser();
        warehouseUser.setUserId(userId + "");
        warehouseUser.setWarehouseId(libId);
        List<WarehouseUser> warehouseUserList = warehouseUserMapper.select(warehouseUser);
        if (warehouseUserList == null || warehouseUserList.size() == 0) {
            warehouseUserMapper.insert(warehouseUser);
        }
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        r.setData(new JSONObject());
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result delWarehouseUser(Integer userId, Integer libId,String appId, HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        // 检查删除lib的appId
        OffLineFace offLineFace = offlineFaceBiz.getOfflineFaceValidByIdAndAppId(libId,appId);
        if (offLineFace == null) {
            return Result.error(Constants.PERMISSION_CODE_ERROR, requestContext.getMessage("xxbz"));
        }
        Result r = new Result();
        WarehouseUser warehouseUser = new WarehouseUser();
        warehouseUser.setUserId(userId + "");
        warehouseUser.setWarehouseId(libId);
        warehouseUserMapper.delete(warehouseUser);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        r.setData(new JSONObject());
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }

    @Override
    public Result warehouseUserList(Integer userId,String appId, HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();
        Example example = new Example(WarehouseUser.class);
        if (userId != null) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId",userId);
        }
        List<WarehouseUser> warehouseUserList = warehouseUserMapper.selectByExample(example);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("warehouseUserList", warehouseUserList);
        jsonObject.put("userId", userId);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        r.setData(jsonObject);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }
}
