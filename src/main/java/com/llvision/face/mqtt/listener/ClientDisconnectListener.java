package com.llvision.face.mqtt.listener;

import com.llvision.face.mqtt.event.ClientDisconnectEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 客户端断开监听
 * @Author yudong
 * @Date 2018年12月27日 16:16
 */
@Component
@Slf4j
public class ClientDisconnectListener implements ApplicationListener<ClientDisconnectEvent> {

    @Override
    public void onApplicationEvent(ClientDisconnectEvent clientDisconnectEvent) {
        log.debug("客户端断开:"+clientDisconnectEvent.getClients().toString());
    }
}
