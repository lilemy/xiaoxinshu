package com.lilemy.xiaoxinshu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 接口文档信息配置
 *
 * @author lilemy
 * @date 2025-07-21 23:55
 */
@Profile("dev")
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("小新书接口文档")
                        .description("这是一个 API 接口简介")
                        .version("0.0.1-SNAPSHOT")
                        .termsOfService("https://www.lilemy.cn")
                        .contact(new Contact().name("lilemy").url("https://github.com/lilemy").email("2331845269@qq.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }

}