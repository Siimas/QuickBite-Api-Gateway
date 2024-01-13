package com.quickbite.gateway.Security;

import com.quickbite.gateway.Components.JwtUtils;
import com.quickbite.gateway.Components.RouterValidator;
import com.quickbite.gateway.Entities.User.User;
import com.quickbite.gateway.Entities.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

//        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            final String token = this.getAuthHeader(request).substring(7);
            final String tokenEmail = jwtUtils.extractUsername(token);
            final User user = userRepository.findByEmail(tokenEmail)
                    .orElseThrow(() -> new EntityNotFoundException("User not found."));

            if (!jwtUtils.validateToken(token, user.getEmail())) {
                this.onError(exchange, HttpStatus.FORBIDDEN);
            }

            this.updateRequest(exchange, tokenEmail);
//        }

        return chain.filter(exchange);
    }

    private Boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private void updateRequest(ServerWebExchange exchange, String email) {
        exchange.getRequest().mutate().header("email", email).build();
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

}
