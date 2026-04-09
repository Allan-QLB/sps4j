package io.github.sps4j.springboot3.config;

import io.github.sps4j.springboot3.web.Sps4jTomcatWebServerFactory;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = ServletWebServerFactoryAutoConfiguration.class)
@ConditionalOnExpression("T(java.lang.Thread).currentThread().contextClassLoader instanceof T(io.github.sps4j.core.load.Sps4jPluginClassLoader)")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(Tomcat.class)
public class TomcatAutoConfiguration {

    @Bean
    public ServletWebServerFactory webServerFactory() {
        return new Sps4jTomcatWebServerFactory(Thread.currentThread().getContextClassLoader());
    }

}
