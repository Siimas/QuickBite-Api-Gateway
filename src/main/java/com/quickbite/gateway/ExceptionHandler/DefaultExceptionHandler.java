package com.quickbite.gateway.ExceptionHandler;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.LocalDateTime;

@Configuration
public class DefaultExceptionHandler {

    @Bean
    public GlobalFilter errorFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange)
                    .onErrorResume(SignatureException.class, e -> handleException(exchange, e));
        };
    }

    private Mono<Void> handleException(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        ApiError apiError = new ApiError(
                exchange.getRequest().getURI().getPath(),
                throwable.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );

        byte[] errorMessageBytes = apiError.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(errorMessageBytes);

        return response.writeWith(Mono.just(buffer));
    }
}
