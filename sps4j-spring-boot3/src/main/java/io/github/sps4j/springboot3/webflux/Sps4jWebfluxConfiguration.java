package io.github.sps4j.springboot3.webflux;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration(before = HttpHandlerAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnExpression("!(T(java.lang.Thread).currentThread().contextClassLoader instanceof T(io.github.sps4j.core.load.Sps4jPluginClassLoader))")
@AllArgsConstructor
public class Sps4jWebfluxConfiguration {

    private WebFluxProperties webFluxProperties;
    private ApplicationContext applicationContext;

    @Bean("Sps4jCompositeHandler")
    public HttpHandler handler() {
        HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(this.applicationContext).build();
        Map<PluginPathContext, HttpHandler> handlerMap = new HashMap<>();
        if (!StringUtils.hasText(webFluxProperties.getBasePath())) {
            handlerMap.put(PluginPathContext.builder()
                .context("/")
                .prefix("")
                .build(), httpHandler);
        } else {
            handlerMap.put(PluginPathContext.builder()
                .context(webFluxProperties.getBasePath())
                .prefix("")
                .build(), httpHandler);
        }
        return new Sps4jContextPathCompositeHandler(handlerMap);
    }

}
