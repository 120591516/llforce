
package com.llvision.face.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "face_record")
@Data
public class FaceRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * 聚合父Id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 采集照片
     */
    private String pic;

    /**
     * 比对照片url
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
    @Column(name = "is_warning")
	private Integer isWarning;

    /**
     * 人员
     */
    @Column(name = "person_id")
	private Long personId;

    /**
     * 姓名
     */
    @Column(name = "person_name")
    private String personName;

    @Column(name = "person_card")
    private String personCard;

    @Column(name = "warning_type")
    private String warningType;

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
     * 最后识别时间
     */
    @Column(name = "last_record_time")
    private Date lastRecordTime;

    /**
     * 0为正常 1为删除
     */
    @Column(name = "is_del")
    private Integer isDel;

    @Column(name = "bg_image")
    private String bgImage;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "bg_name")
    private String bgName;

    private Double longitude;

    private Double latitude;

    @Column(name = "bg_image_id")
    private String bgImageId;

    @Column(name = "app_record_id")
    private String appRecordId;

    @Column(name = "app_id")
    private String appId;
}