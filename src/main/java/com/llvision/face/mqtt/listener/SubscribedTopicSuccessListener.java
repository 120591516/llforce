package com.llvision.face.mqtt.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.stereotype.Component;

/**
 * 订阅主题成功
 * @Author yudong
 * @Date 2018年12月27日 16:13
 */
@Component
@Slf4j
public class SubscribedTopicSuccessListener implements ApplicationListener<MqttSubscribedEvent> {

    @Override
    public void onApplicationEvent(MqttSubscribedEvent mqttSubscribedEvent) {
        log.debug("订阅主题成功:"+mqttSubscribedEvent.getMessage());
    }
}
