package io.github.sps4j.springboot2.webflux;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnExpression("!(T(java.lang.Thread).currentThread().contextClassLoader instanceof T(io.github.sps4j.core.load.Sps4jPluginClassLoader))")
@AutoConfigureBefore(HttpHandlerAutoConfiguration.class)
@AllArgsConstructor
public class Sps4jWebfluxConfiguration {

    private WebFluxProperties webFluxProperties;
    private ApplicationContext applicationContext;

    @Bean("Sps4jCompositeHandler")
    public HttpHandler handler() {
        HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(this.applicationContext).build();
        Map<String, HttpHandler> handlerMap = new HashMap<>();
        if (!StringUtils.hasText(webFluxProperties.getBasePath())) {
            handlerMap.put("/", httpHandler);
        } else {
            handlerMap.put(webFluxProperties.getBasePath(), httpHandler);
        }
        return new Sps4jContextPathCompositeHandler(handlerMap);
    }

}
