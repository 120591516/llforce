package com.llvision.face.dao.biz;

import com.llvision.face.base.BaseBiz;
import com.llvision.face.dao.mapper.OffLineFaceMapper;
import com.llvision.face.entity.OffLineFace;
import com.llvision.face.utils.PagedLimitUtils;
import com.llvision.face.utils.PagedModelList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 16:09
 * @Version 1.0
 */
@Service
@Slf4j
public class OfflineFaceBiz extends BaseBiz<OffLineFaceMapper, OffLineFace> {

    public boolean checkDelByUserIdAndName(String name, Integer userId,Integer type,String appId) {
        int count = mapper.checkDelByUserIdAndName(userId, name,type,appId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    public OffLineFace getOfflineFaceValidByIdAndAppId(Integer id,String appId) {
        return mapper.getOfflineFaceValidByIdAndAppId(id,appId);
    }

    public int del(Integer id,String appId) {
        return mapper.delOfflineFace(id,appId);
    }

    public List<OffLineFace> selectOfflineFaces(Integer userId,Integer type,String appId) {
        return mapper.selectOfflineFaces(userId,type,appId);
    }


    public List<Integer> getAvailableLibIds(Integer userId, String appId, int type) {
        return mapper.getAvailableLibIds(userId,appId,type);
    }
}
