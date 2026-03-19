package io.github.sps4j.springboot2.webflux;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;


@Getter
public class Sps4jContextPathCompositeHandler implements HttpHandler {

    private final ConcurrentNavigableMap<PluginPathContext, HttpHandler> handlerMap;


    public Sps4jContextPathCompositeHandler(Map<PluginPathContext, ? extends HttpHandler> handlerMap) {
        Assert.notEmpty(handlerMap, "Handler map must not be empty");
        this.handlerMap = initHandlers(handlerMap);
    }

    private static ConcurrentNavigableMap<PluginPathContext, HttpHandler> initHandlers(Map<PluginPathContext, ? extends HttpHandler> map) {
        map.keySet().stream().map(PluginPathContext::getContext).forEach(Sps4jContextPathCompositeHandler::assertValidContextPath);
        ConcurrentNavigableMap<PluginPathContext, HttpHandler> m = new ConcurrentSkipListMap<>();
        m.putAll(map);
        return m;
    }

    private static void assertValidContextPath(String contextPath) {
        Assert.hasText(contextPath, "Context path must not be empty");
        if (contextPath.equals("/")) {
            return;
        }
        Assert.isTrue(contextPath.startsWith("/"), "Context path must begin with '/'");
        Assert.isTrue(!contextPath.endsWith("/"), "Context path must not end with '/'");
    }


    @Override
    public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
        String path = request.getPath().value();
        Set<Map.Entry<PluginPathContext, HttpHandler>> entries = handlerMap.entrySet();
        for (Map.Entry<PluginPathContext, HttpHandler> entry : entries) {
            String key = entry.getKey().getContext();
            if (path.startsWith(key)) {
                request = request.mutate()
                        .path(path.substring(entry.getKey().getPrefix().length()))
                        .contextPath("/")
                        .build();
                return entry.getValue().handle(request, response);
            }
        }
        response.setStatusCode(HttpStatus.NOT_FOUND);
        return response.setComplete();
    }


}
