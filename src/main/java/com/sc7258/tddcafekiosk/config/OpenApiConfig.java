//package com.sc7258.tddcafekiosk.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.parser.OpenAPIV3Parser;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.util.StreamUtils;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//public class OpenApiConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() throws IOException {
//        ClassPathResource resource = new ClassPathResource("static/openapi/cafe-kiosk-api-v1.yaml");
//        String yamlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
//        return new OpenAPIV3Parser().readContents(yamlContent).getOpenAPI();
//    }
//}
