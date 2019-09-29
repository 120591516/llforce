package com.llvision.face.config;

import com.llvision.face.dao.mapper.SystemConfMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * 预加载配置，不用
 * @Author: guoyc
 * @Date: 2019/5/17 15:28
 * @Version 1.0
 */

@Slf4j
public class MqttServerRunner implements CommandLineRunner {

    @Resource
    private SystemConfMapper systemConfMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("MqttServerRunner runner....:{}",systemConfMapper.selectAll().get(0));
    }
}
