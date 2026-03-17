package io.github.sps4j.springboot2.webflux;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnExpression("T(java.lang.Thread).currentThread().contextClassLoader instanceof T(io.github.sps4j.core.load.Sps4jPluginClassLoader)")
@AutoConfigureBefore(ReactiveWebServerFactoryAutoConfiguration.class)
@AllArgsConstructor
public class Sps4jPluginWebfluxConfiguration {

    private WebFluxProperties webFluxProperties;

    @Bean
    public ReactiveWebServerFactory nettyWebServerFactory() {
        return new Sps4jNettyWebServerFactory(webFluxProperties);
    }
}
