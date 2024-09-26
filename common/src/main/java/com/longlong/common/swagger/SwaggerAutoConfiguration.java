//package com.longlong.common.swagger;
//
//
//
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Profile;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * swagger配置
// */
//
//@EnableSwagger2
//@Profile({"dev","test"})
//@Configuration
//public class SwaggerAutoConfiguration {
//    @Bean
//    @ConditionalOnMissingBean
//    public SwaggerProperties swaggerProperties() {
//        return new SwaggerProperties();
//    }
//
//    @Bean
//    public Docket createApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(myApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.longlong.controller"))
//                .paths(PathSelectors.any())
//                .build();
//
//    }
//
//    private ApiInfo myApiInfo() {
//        return new ApiInfoBuilder()
//                .title(swaggerProperties().getTitle())
//                .description(swaggerProperties().getDescription())
//                .termsOfServiceUrl(swaggerProperties().getTermsOfServiceUrl())
//                .version(swaggerProperties().getVersion())
//                .build();
//    }
//
//}
