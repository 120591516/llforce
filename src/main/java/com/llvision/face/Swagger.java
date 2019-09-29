package com.llvision.face;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author: guoyc
 * @Date: 2019/5/15 15:09
 * @Version 1.0
 */
@Configuration
@Slf4j
public class Swagger {

    @Value("${faceService.api.version}")
    private String apiVersion;

    @Value("${faceService.api.desc}")
    private String apiDesc;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.llvision.face.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        log.info("Swagger start.......");
        return new ApiInfoBuilder().title("人脸识别").description(apiDesc).termsOfServiceUrl("http://llvision.com")
                .contact("sssss").version(apiVersion).build();
    }
}
