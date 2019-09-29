package com.llvision.face.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sftp")
public class SftpConfig {
    /**
     * 服务器地址
     */
    private  String host;
    /**
     * 账号
     */
    private  String username;
    /**
     * 密码
     */
    private  String password;
    /**
     * 端口
     */
    private  int port;
    /**
     * 路径
     */
    private  String remotePath;


    @Override
    public String toString() {
        return "SftpConfig{" +
                "host='" + host + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                ", remotePath='" + remotePath + '\'' +
                '}';
    }
}
