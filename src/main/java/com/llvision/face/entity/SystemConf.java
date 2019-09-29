package com.llvision.face.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Table(name = "system_conf")
@Accessors(chain = true)
@Data
public class SystemConf {
    @Id
    private Integer id;

    /**
     * 清晰度
     */
    private Integer sharpness;

    /**
     * 角度
     */
    private Integer angle;

    /**
     * 识别阈值
     */
    private Integer similarity;

    /**
     * 采集模式（1，单脸 2，多脸）
     */
    @Column(name = "collect_mode")
    private Integer collectMode;

    /**
     * 比对模式（1，离线 ， 2 在线， 3，离/在线）
     */
    @Column(name = "compare_mode")
    private Integer compareMode;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_secret")
    private String appSecret;

    @Column(name = "api_host")
    private String apiHost;

    @Column(name = "mqtt_host")
    private String mqttHost;


}