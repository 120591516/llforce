package com.llvision.face.mqtt.handler;

/**
 * @Author yudong
 * @Date 2018年12月27日 17:20
 */
public interface MessageHandler<T> {

    /**
     * 是否能处理
     * @param t
     * @return
     */
    boolean isProcessing(T t);

    /**
     * 处理
     * @param t
     */
    void processing(T t);

}
