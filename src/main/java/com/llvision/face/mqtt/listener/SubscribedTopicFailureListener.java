package com.llvision.face.mqtt.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.stereotype.Component;

/**
 * 连接失败或者订阅失败
 * @Author yudong
 * @Date 2018年12月27日 16:16
 */
@Component
@Slf4j
public class SubscribedTopicFailureListener implements ApplicationListener<MqttConnectionFailedEvent> {

    @Override
    public void onApplicationEvent(MqttConnectionFailedEvent mqttConnectionFailedEvent) {
        log.debug("订阅主题失败:"+mqttConnectionFailedEvent.toString());
    }
}
