package com.llvision.face.mqtt.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * 接收消息处理
 */
@Slf4j
@Component
public class FaceMsgProcessor implements  Processor<Object>{


    @Override
    public boolean isProcessing(Object s) {
        return true;
    }

    @Override
    public void processing(Object s) {
        log.debug("receive mqtt msg:{}",s.toString());
    }
}
