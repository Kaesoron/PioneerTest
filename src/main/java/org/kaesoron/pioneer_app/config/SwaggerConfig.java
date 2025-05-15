package org.kaesoron.pioneer_app.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankAppOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Bank App API")
                        .description("Документация для API банковского сервиса")
                        .version("1.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("bank")
                .pathsToMatch("/api/**")
                .build();
    }
}
