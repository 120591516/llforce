package com.llvision.face.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Table(name = "person")
@Data
public class Person {
    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 照片
     */
    private String pic;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String card;

    /**
     * 性别：0、女，1、男，2未知
     */
    private Integer sex;

    /**
     * 生日
     */
    private String birthday;

    private String nation;

    /**
     * 地址
     */
    private String address;

    /**
     * 16个0为未警示，对应位数有值说明为警示
     */
    @Column(name = "warning_type")
    private String warningType;

    /**
     * 识别模型
     */
    private String model;

    /**
     * 模型版本号
     */
    private String version;

    /**
     * 是否警示
     */
    @Column(name = "is_warning")
    private Integer isWarning;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "create_user")
    private Integer createUser;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新人
     */
    @Column(name = "update_user")
    private Integer updateUser;

    /**
     * 0为正常 1为删除
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 更新标记  用于app端检验第一次更新 默认值不能为0
     */
    private Integer mark;

    /**
     * 操作类型 1 新增 2 编辑 3 删除
     */
    private Integer action;


    /**
     * 人脸特征码
     */
    private String hash;

    /**
     * 数据隔离
     */

}