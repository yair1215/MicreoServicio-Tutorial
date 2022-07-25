package com.tutorial.gatewayservice.config;


import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

//http://localhost:8080/webjars/swagger-ui/index.html
@Configuration
@Slf4j
public class AppConfig {
    private static final String API_URI = "/v3/api-docs";

    private final RouteDefinitionLocator locator;

    public AppConfig(RouteDefinitionLocator locator) {
        this.locator = locator;
    }

    @Bean
    public GroupedOpenApi apis(SwaggerUiConfigProperties swaggerUiConfigProperties) {

        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();

        locator.getRouteDefinitions().subscribe(routeDefinition -> {
            log.info("Discovered route definition: {}", String.valueOf(routeDefinition.getId()));
            String resourceName = routeDefinition.getId();
            String location = String.valueOf(routeDefinition.getPredicates().get(0).getArgs().get("_genkey_0")).replace("/**", API_URI);
            log.info("Adding swagger resource: {} with location: {}", resourceName, (location));
            urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(resourceName, API_URI, resourceName));
        });

        swaggerUiConfigProperties.setUrls(urls);

        return GroupedOpenApi.builder()
                .group("resource")
                .pathsToMatch("/api/**")
                .build();
    }
}