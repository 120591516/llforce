package com.llvision.face.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 发送消息类
 * @Author yudong
 * @Date 2018年12月27日 17:28
 */
@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface SendMessage {


    /**
     * 发送消息
     * @param payload 消息
     */
  void send(String payload);
    /**
     * 发送消息
     * @param payload 消息
     * @param topic  主题
     */
  void send(String payload, @Header( MqttHeaders.TOPIC ) String topic);

    /**
     * 发型消息
     * @param payload 消息
     * @param topic 主题
     * @param qos 0尽力而为。消息发送者会想尽办法发送消息，但是遇到意外并不会重试
     *            1至少一次。消息接收者如果没有知会或者知会本身丢失，消息发送者会再次发送以保证消息接收者至少会收到一次，当然可能造成重复消息
     *            2恰好一次。保证这种语义肯待会减少并发或者增加延时，不过丢失或者重复消息是不可接受的时候，级别2是最合适的
     */
  void send(String payload, @Header( MqttHeaders.TOPIC ) String topic, @Header( MqttHeaders.QOS ) int qos);

}
