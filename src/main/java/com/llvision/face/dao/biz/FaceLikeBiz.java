package com.llvision.face.dao.biz;

import com.llvision.face.base.BaseBiz;
import com.llvision.face.dao.mapper.FaceLikeMapper;
import com.llvision.face.entity.FaceLike;
import com.llvision.face.entity.FaceRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: guoyc
 * @Date: 2019/5/21 10:10
 * @Version 1.0
 */

@Service
@Slf4j
public class FaceLikeBiz extends BaseBiz<FaceLikeMapper, FaceLike> {

    @Resource
    private FaceLikeMapper faceLikeMapper;

    public List<FaceLike> getListByFaceRecordId(Integer faceRecordId) {
        return faceLikeMapper.getListByFaceId(faceRecordId);
    }
}
