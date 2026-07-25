package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// SpringDoc 接口文档配置
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("demo3 API")          // 文档标题
                        .version("1.0.0")             // 版本号
                        .description("demo3 接口文档")  // 文档描述
                        .contact(new Contact().name("admin")));
    }
}
