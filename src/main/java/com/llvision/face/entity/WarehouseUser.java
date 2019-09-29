package com.llvision.face.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "warehouse_user")
@Data
public class WarehouseUser {
    @Id
    private Integer id;

    /**
     * 库id
     */
    @Column(name = "warehouse_id")
    private Integer warehouseId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

}