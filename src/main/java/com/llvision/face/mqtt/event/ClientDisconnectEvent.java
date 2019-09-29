package com.llvision.face.mqtt.event;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;

/**
 * 客户端断开事件
 * @Author yudong
 * @Date 2018年12月27日 17:00
 */
public class ClientDisconnectEvent extends ApplicationEvent {

    @Getter
    private Collection<Client> clients;

    public ClientDisconnectEvent(Object source,Collection<Client> clients) {
        super(source);
        this.clients = clients;
    }

    public ClientDisconnectEvent(Object source,Client client) {
        super(source);
        this.clients = Sets.newHashSet(client);
    }



    @NoArgsConstructor
    @Data
    class Client{
        private String client_id;
        private String username;
        private String ipaddress;
        private int port;
        private boolean clean_sess;
        private int proto_ver;
        private int keepalive;
        private String connected_at;
        }

}
