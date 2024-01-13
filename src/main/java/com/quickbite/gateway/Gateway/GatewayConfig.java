package com.quickbite.gateway.Gateway;

import com.quickbite.gateway.Security.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator customLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("signup-route", r -> r
                        .path("/api/v1/auth/signup")
                        .filters(f -> f.setPath("http://localhost:8081/signup"))
                        .uri("http://localhost:8081/"))
                .route("signin-route", r -> r
                        .path("/api/v1/auth/signin")
                        .filters(f -> f.setPath("http://localhost:8081/signin"))
                        .uri("http://localhost:8081/"))
                .route("refresh-route", r -> r
                        .path("/api/v1/auth/refresh")
                        .filters(f -> f.setPath("http://localhost:8081/refresh"))
                        .uri("http://localhost:8081/"))

                .route("business-logic", r -> r
                        .path("/api/v1/**")
                        .filters(f -> f
                                .filter(authenticationFilter)
                                .rewritePath("/api/v1/(?<remaining>.*)", "/${remaining}")
                        )
                        .uri("http://localhost:8082/"))
                .build();
    }
}
