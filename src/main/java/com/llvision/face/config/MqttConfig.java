package com.llvision.face.config;

import com.llvision.face.mqtt.entity.WillMessage;
import com.llvision.face.mqtt.handler.MessageObjectHandler;
import com.llvision.face.mqtt.processor.Processor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.UUID;

/**
 * @Author yudong
 * @Date 2018年12月26日 14:40
 */
@Configuration
public class MqttConfig {

/*    @Value("${listener.appid}")
    private  String appid;*/
    /**
     * mqtt参数配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.mqtt")
   public MqttConnectOptions options(WillMessage will){
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setKeepAliveInterval(30);
        connectOptions.setWill(will.getTopic(),will.getPayload().getBytes(),will.getQos(),will.getRetained());
        return connectOptions;
    }

    /**
     * 连接工厂
     * @param connectOptions
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(MqttConnectOptions connectOptions){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(connectOptions);
        return factory;
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.mqtt.will")
    public WillMessage willMessage(){
        return new WillMessage();
    }


    /**
     * 管道配置
     * @return
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * 发送消息
     * 配置: 异步:默认topic:qos1
     * @param mqttPahoClientFactory
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(UUID.randomUUID().toString(), mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("topic");
        messageHandler.setDefaultQos(1);
        return messageHandler;
    }


    /**
     * 接收消息处理器
     * 5秒重连一次
     * Qos 1
     * @param options
     * @param mqttPahoClientFactory
     * @return
     */
    @Bean
    public MessageProducer inbound(MqttConnectOptions options, MqttPahoClientFactory mqttPahoClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(UUID.randomUUID().toString(), mqttPahoClientFactory,"topic", "faceserver");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * 消息接收处理
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(WebApplicationContext webApplicationConnect) {
        Collection<Processor> processorIterator = webApplicationConnect.getBeansOfType(Processor.class).values();
       return new MessageObjectHandler(processorIterator);
    }




}
