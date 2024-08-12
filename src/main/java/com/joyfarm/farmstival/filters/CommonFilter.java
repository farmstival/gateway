package com.joyfarm.farmstival.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class CommonFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String host = request.getRemoteAddress().getHostString();
        ServerWebExchange ex = exchange.mutate().request(exchange.getRequest().mutate()
                        .header("from-gateway", "true") //게이트웨이를 통해서 유입됨
                        .header("gateway-host", host) //유입된 게이트웨이 주소
                        .build())
                .build();

        return chain.filter(ex);
    }
}
