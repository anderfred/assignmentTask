package com.anderfred.assignmenttask;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssignmentTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentTaskApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("12") String appVersion) {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Customer accounts API").version(appVersion)
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
