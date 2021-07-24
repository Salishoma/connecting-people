package com.connect.connectingpeople.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
public class SpringfoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, newArrayList(
                        new ResponseBuilder().code("500")
                                .description("Server error").build(),
                        new ResponseBuilder().code("403")
                                .description("Request not permitted!!").build()
                ));

    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Social Connect",
                "A website where people can connect to friends and loved ones.",
                "API TOS",
                "Terms of service",
                new Contact("Abubakar Salifu", "", "abubakaroma91@yahoo.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
