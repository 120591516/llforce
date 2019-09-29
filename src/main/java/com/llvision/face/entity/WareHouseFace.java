package com.llvision.face.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "warehouse_face")
@Data
public class WareHouseFace {
    /**
     * 离线库人像信息表id
     */
    @Id
    private Integer id;

    /**
     * 离线库id
     */
    @Column(name = "lib_id")
    private Integer libId;

    /**
     * 身份信息
     */
    @Column(name = "person_id")
    private Long personId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_user")
    private Integer createUser;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_user")
    private Integer updateUser;

    /**
     * 0为正常 1为删除
     */
    @Column(name = "is_del")
    private Integer isDel;
}