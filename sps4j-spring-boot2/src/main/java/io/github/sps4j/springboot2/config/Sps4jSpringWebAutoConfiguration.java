package io.github.sps4j.springboot2.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(Sps4jSpringWebProperties.class)
public class Sps4jSpringWebAutoConfiguration {
}
