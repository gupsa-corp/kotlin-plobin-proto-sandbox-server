package com.plobin.sandbox.Config.Swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Plobin Sandbox API")
                    .description("Plobin 샌드박스 서버 API 문서")
                    .version("v1.0.0")
                    .contact(
                        Contact()
                            .name("Plobin Team")
                            .email("contact@plobin.com")
                    )
            )
    }
}