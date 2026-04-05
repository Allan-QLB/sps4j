package io.github.sps4j.springboot3.config;

import io.github.sps4j.springboot3.web.AddPluginServletContextPathCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnExpression("T(java.lang.Thread).currentThread().contextClassLoader instanceof T(io.github.sps4j.core.load.Sps4jPluginClassLoader)")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureBefore({ServletWebServerFactoryAutoConfiguration.class, JettyAutoConfiguration.class,
    TomcatAutoConfiguration.class})
public class Sps4jSpringMvcAutoConfiguration {

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(prefix = "sps4j.spring-mvc", name = "add-servlet-context-prefix", havingValue = "true", matchIfMissing = true)
    public AddPluginServletContextPathCustomizer addPluginServletContextPathCustomizer(ServerProperties serverProperties) {
        return new AddPluginServletContextPathCustomizer(serverProperties);
    }

}
