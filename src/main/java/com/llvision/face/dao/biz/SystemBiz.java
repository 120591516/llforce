package com.llvision.face.dao.biz;

import com.alibaba.fastjson.JSONObject;
import com.llvision.face.base.BaseBiz;
import com.llvision.face.dao.mapper.SystemConfMapper;
import com.llvision.face.entity.SystemConf;
import com.llvision.face.service.WatchDogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: guoyc
 * @Date: 2019/5/17 11:30
 * @Version 1.0
 */
@Service
@Slf4j
public class SystemBiz extends BaseBiz<SystemConfMapper, SystemConf> {

    @Resource
    private WatchDogService watchDogService;

    public int updateSystemConfig(SystemConf systemConf) {
        return mapper.updateSystemConfig(systemConf);
    }

    public SystemConf getSystemConf (SystemConf conf) {
        SystemConf systemConf = mapper.selectOne(conf);
        JSONObject decryptJson = watchDogService.decryptFile();
        log.info("decryptJson:{}",decryptJson);
        String compareMode = decryptJson.getString("compareMode") + "";
        String collectMode = decryptJson.getString("collectMode") + "";
        if (systemConf != null) {
            if (StringUtils.isNotBlank(compareMode) && !"null".equals(compareMode)) {
                systemConf.setCompareMode(Integer.parseInt(compareMode));
            }

            if (StringUtils.isNotBlank(collectMode) && !"null".equals(collectMode)) {
                systemConf.setCollectMode(Integer.parseInt(collectMode));
            }
        }
        return systemConf;
    }
}
