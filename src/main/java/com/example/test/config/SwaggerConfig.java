package com.example.test.config;

import io.swagger.model.ApiInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableSwagger2
public class SwaggerConfig {
    String path="com.example.test.rest";
    /*@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(path))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Test Swagger 实例文档",
                "Test",
                "1.0.0",
                "Terms of service",
                "sy",null,null);
    }*/
}
