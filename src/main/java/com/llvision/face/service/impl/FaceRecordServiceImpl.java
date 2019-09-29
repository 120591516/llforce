package com.llvision.face.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.llvision.face.dao.mapper.FaceRecordMapper;
import com.llvision.face.entity.FaceRecord;
import com.llvision.face.service.IFaceRecordService;
import com.llvision.face.utils.PageUtil;
import com.llvision.face.utils.StringUtils;
import com.llvision.face.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
 * @Date: 2019/5/21 10:12
 * @Version 1.0
 */
@Service
@Transactional
@Slf4j
public class FaceRecordServiceImpl implements IFaceRecordService {

    @Resource
    private FaceRecordMapper faceRecordMapper;

    @Value("${pic_url}")
    private String pic_url;

    @Override
    public Result faceRecordList(Integer userId, Integer pageSize,Integer pageNum,String appId,HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        Result r = new Result();
        Example example = new Example(FaceRecord.class);
        example.setOrderByClause("create_time desc");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",0);
        criteria.andEqualTo("appId",appId);
        if (userId != null) {
            criteria.andEqualTo("createUser",userId);
        }
        Map<String,Integer> page = PageUtil.getPageInfo(pageSize + "", pageNum + "");
        PageHelper.startPage(page.get("pageIndex"), page.get("pageSize"));
        List<FaceRecord> faceRecordList = faceRecordMapper.selectByExample(example);

        if (faceRecordList != null && faceRecordList.size() > 0) {
            faceRecordList.forEach(faceRecord -> {
                faceRecord.setPic(StringUtils.isNotEmpty(faceRecord.getPic()) ? pic_url + faceRecord.getPic() : "");
                faceRecord.setComparePic(StringUtils.isNotEmpty(faceRecord.getComparePic()) ? pic_url + faceRecord.getComparePic() : "");
            });
        }

        PageInfo pageInfo = new PageInfo(faceRecordList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("faceRecordList", faceRecordList);
        jsonObject.put("pageTotal", pageInfo.getPageSize());
        jsonObject.put("pageNum", pageInfo.getPageNum());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("userId", userId);
        r.setMsg(requestContext.getMessage("qqcg"));
        r.setCode(0);
        r.setData(jsonObject);
        JSONObject resultJson = (JSONObject) JSON.toJSON(r);
        log.info("method:{} result:{}",request.getRequestURI(), resultJson.toJSONString());
        return r;
    }
}
