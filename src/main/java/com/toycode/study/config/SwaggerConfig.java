package com.toycode.study.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = SwaggerConfig.PROPERTIES_PREFIX)
public class SwaggerConfig {

    public static final String PROPERTIES_PREFIX = "springdoc.properties";
    private static final String API_KEY_NAME = "Token";

    private String title;
    private String version;
    private String description;
    private String name;
    private String email;
    private String url;

    @Bean
    public OpenAPI openAPI(@Value("${app.security.enabled}") boolean isEnabled) {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(getInfo());
        if (isEnabled) {
            openAPI.components(getComponets())
                .addSecurityItem(
                    new SecurityRequirement()
                        .addList(API_KEY_NAME));
        }
        return openAPI;
    }

    private Info getInfo() {
        return new Info()
            .title(title)
            .version(version)
            .description(description)
            .contact(getContact());
    }

    private Contact getContact() {
        return new Contact()
            .name(name)
            .url(url)
            .email(email);
    }

    private Components getComponets() {
        return new Components()
            .addSecuritySchemes(
                API_KEY_NAME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"));
    }
}
