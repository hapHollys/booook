package com.haphollys.booook.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.DocExpansion
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger.web.UiConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun movieApiV1(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Movie V1")
            .apiInfo(ApiInfoBuilder().title("Movie API V1").build())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/v1/movies/**"))
            .build()
    }

    @Bean
    fun screenApiV1(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Screen V1")
            .apiInfo(ApiInfoBuilder().title("Screen API V1").build())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/v1/screens/**"))
            .build()
    }

    @Bean
    fun bookApiV1(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Book V1")
            .apiInfo(ApiInfoBuilder().title("Book API V1").build())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/v1/books/**"))
            .build()
    }

    @Bean
    fun paymentApiV1(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Payment V1")
            .apiInfo(ApiInfoBuilder().title("Payment API V1").build())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/v1/payments/**"))
            .build()
    }

    @Bean
    fun swaggerUiConfiguration(): UiConfiguration? {
        return UiConfigurationBuilder.builder().docExpansion(DocExpansion.LIST).build()
    }
}
