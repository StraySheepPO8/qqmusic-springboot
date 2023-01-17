package com.yahoo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

//@EnableSwagger2
@EnableOpenApi
@Configuration
public class SwaggerConfig {


    @Bean
    public Docket docket0(Environment environment) {
        Profiles profiles = Profiles.of("dev");
        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("0全部接口")
                .apiInfo(getApiInfo())
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yahoo.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    @Bean
    public Docket docket1(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1第一组：user/用户接口")
                .apiInfo(getApiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yahoo.controller"))
                .paths(PathSelectors.ant("/user/**"))
                .build();
    }


    @Bean
    public Docket docket2(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("2第二组：song/歌曲接口")
                .apiInfo(getApiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yahoo.controller"))
                .paths(PathSelectors.ant("/song/**"))
                .build();
    }


    @Bean
    public Docket docket3(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("3第三组：songList/歌单接口")
                .apiInfo(getApiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yahoo.controller"))
                .paths(PathSelectors.ant("/list/*"))
                .build();
    }


    @Bean
    public Docket docket4(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("4第四组：userLike/用户喜欢接口")
                .apiInfo(getApiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yahoo.controller"))
                .paths(PathSelectors.ant("/userLike/**"))
                .build();
    }


    private ApiInfo getApiInfo() {
        Contact contact = new Contact("李家林", "url:localhost", "wocsom@qq.com");

        return new ApiInfo(
                "李家林的swagger文档",
                "我的灵魂像上午的群山",
                "v1.0",
                "localhost:8080",
                contact,
                "apache 2.0",
                "licenseUrl",
                new ArrayList<>()
        );
    }


}
