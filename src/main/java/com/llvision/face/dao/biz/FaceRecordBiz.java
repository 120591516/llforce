package com.llvision.face.dao.biz;

import com.llvision.face.utils.PagedLimitUtils;
import com.llvision.face.utils.PagedModelList;
import org.springframework.stereotype.Service;

import com.llvision.face.base.BaseBiz;
import com.llvision.face.dao.mapper.FaceRecordMapper;
import com.llvision.face.entity.FaceRecord;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: guoyc
 * @Date: 2019/5/21 10:10
 * @Version 1.0
 */

@Service
@Slf4j
public class FaceRecordBiz extends BaseBiz<FaceRecordMapper, FaceRecord> {

    @Resource
    private FaceRecordMapper faceRecordMapper;

    public PagedModelList<FaceRecord> getAppPageListByWhere(String  strWhere, Integer pageId, Integer pageSize) {
        int count = faceRecordMapper.getCountByWhere(strWhere);
        List<FaceRecord> list = faceRecordMapper.getPageListByWhere(strWhere, PagedLimitUtils.getPagedIndex(pageId, pageSize), PagedLimitUtils.getPagedLimit(pageId, pageSize));
        PagedModelList<FaceRecord> pml = new PagedModelList<FaceRecord>(pageId, pageSize, count);
        pml.addModels(list);
        return pml;
    }

    public List<FaceRecord> getListByParentIdSimilarityDesc(Integer id) {
        return faceRecordMapper.getListByParentIdSimilarityDesc(id);
    }

    public List<Map<String,Object>> getResultList(String resultImageName) {
        return faceRecordMapper.getResultlist(resultImageName);
    }

    public FaceRecord selectInfo(String appRecordId, Integer recordId, String appId) {
        return faceRecordMapper.selectInfo(appRecordId,recordId,appId);
    }


}
