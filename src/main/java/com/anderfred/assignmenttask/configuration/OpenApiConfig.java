package com.anderfred.assignmenttask.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 *      OpenApi 3 / Swagger setup
 *      Exposed json format endpoint http://localhost:8080/v3/api-docs
 */

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("1") String appVersion) {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Orders task").version(appVersion)
                        .license(new License().name("_").url("http://localhost:8080")));
    }
}
