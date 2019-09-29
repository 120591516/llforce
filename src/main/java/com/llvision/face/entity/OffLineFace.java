package com.llvision.face.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "offline_face")
@Data
public class OffLineFace {
    @Id
    private Integer id;

    /**
     * 库名称
     */
    private String name;

    /**
     * 0未激活 1激活
     */
    private Integer status;

    @Column(name = "create_user")
    private Integer createUser;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_user")
    private Integer updateUser;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 1为删除
     */
    @Column(name = "is_del")
    private Integer isDel;

    /**
     * 1:  人像   2：车牌(预留)
     */
    private Integer type;

    /**
     * 数据隔离
     */
    @Column(name = "app_id")
    private String appId;

}