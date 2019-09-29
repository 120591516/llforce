package com.llvision.face.mqtt.entity;

import lombok.Data;

/**
 * @Author yudong
 * @Date 2018年12月28日 10:41
 */
@Data
public class WillMessage {


    /**
     * 主题
     */
    private String topic;

    /**
     * 消息
     */
    private String payload;

    /**
     * qos
     */
    private int qos;

    /**
     * 是否永久
     */
    private Boolean retained;



}
