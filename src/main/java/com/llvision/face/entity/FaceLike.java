package com.llvision.face.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "face_like")
@Data
public class FaceLike {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * 对应识别记录
     */
    @Column(name = "face_recoed_id")
    private Integer faceRecoedId;

    /**
     * 对比图片
     */
    @Column(name = "compare_pic")
    private String comparePic;

    /**
     * 相似度
     */
    private Double similarity;

    /**
     * 是否警示
     */
    private Integer status;

    /**
     * 人员信息
     */
    @Column(name = "person_id")
    private Long personId;

    /**
     * 姓名
     */
    @Column(name = "person_info")
    private String personInfo;

    /**
     * 16个0为未警示，对应位数有值说明为警示
     */
    @Column(name = "warning_type")
    private String warningType;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 0为正常 1为删除
     */
    @Column(name = "is_del")
    private Integer isDel;

}