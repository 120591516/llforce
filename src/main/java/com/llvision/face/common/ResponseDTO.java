package com.llvision.face.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 公共返回参数实体类
 * @Author yudong
 * @Date 2018年07月02日 下午5:21
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDTO<T extends Serializable> implements Serializable {

 private static final long serialVersionUID = -2869850010092953027L;

    public ResponseDTO() {
    }

    public ResponseDTO(@NotNull(message = "{responsedto.message}") String message, @NotNull(message = "{responsedto.code}") int code) {
        this("",0,"","","","",message,code,null);
    }

    /**
     * 版本号
     */
    @NotNull(message = "{responsedto.version}")
    private String version;
    /**
     * 指令序号
     */
    @NotNull(message = "{responsedto.seqnum}")
 private Integer seqnum;
    /**
     * 开始地址
     */
    @NotNull(message = "{responsedto.from}")
 private String from;
    /**
     * 目的地址
     */
    @NotNull(message = "{responsedto.to}")
 private String to;
    /**
     * 类型根据type的类型来确定访问者的类型
     */
    @NotNull(message = "{responsedto.type}")
 private String type;
    /**
     * 设备号
     */
    @NotNull(message = "{responsedto.number}")
 private String number;
    /**
     * 消息开发者
     */
    @NotNull(message = "{responsedto.message}")
 private String message;
    /**
     * 状态码
     */
    @NotNull(message = "{responsedto.code}")
 private Integer code;
    /**
     * 接口数据
     */
 private T data;

    /**
     * 复制对象
     * @return
     */
    public ResponseDTO copy(){
        ResponseDTO responseDTO = new ResponseDTO();
        BeanUtils.copyProperties(this,responseDTO);
        return responseDTO;
    }
    
    /**
     * 判断请求是否成功
     * @return
     */
    public boolean ok(){
        return this.getCode()== 0?true:false;
    }
}
