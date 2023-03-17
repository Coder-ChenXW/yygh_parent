package com.atguigu.yygh.gateway.filter;


import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 * 全局Filter，统一处理会员登录与外部不允许访问的服务
 * </p>
 */

@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();

        //api接口，校验必须登录
        if (antPathMatcher.match("/admin/user/**", path)) {

            return chain.filter(exchange);

        } else {
            List<String> strings = request.getHeaders().get("X-Token");
            if (strings== null) {

                ServerHttpResponse response = exchange.getResponse();

                response.setStatusCode(HttpStatus.SEE_OTHER);

                response.getHeaders().set(HttpHeaders.LOCATION,"http://localhost:9528");

                return response.setComplete();
            } else {
                return chain.filter(exchange);
            }
        }
    }

    @Override
    public int getOrder() {

        return 0;

    }


}