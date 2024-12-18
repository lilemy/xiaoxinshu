package cn.lilemy.xiaoxinshugateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        if (!antPathMatcher.match("/api/interface/**", path)) {
            return chain.filter(exchange);
        }
        String method = Objects.requireNonNull(request.getMethod()).toString();
        String requestId = request.getId();
        log.info("请求唯一标识：{}", requestId);
        log.info("请求路径：{}", path);
        log.info("请求方法：{}", method);
        log.info("请求参数：{}", request.getQueryParams());
        String sourceAddress = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        log.info("请求来源地址：{}", sourceAddress);
        log.info("请求来源地址：{}", request.getRemoteAddress());
        // 2. 访问控制 - 黑白名单
        // 3. 用户鉴权（判断 ak、sk 是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = URLDecoder.decode(Objects.requireNonNull(headers.getFirst("body")), UTF_8);// 解决中文乱码
        log.info("请求信息：accessKey：{}，nonce：{}，timestamp：{}，sign：{}，body：{}", accessKey, nonce, timestamp, sign, body);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
