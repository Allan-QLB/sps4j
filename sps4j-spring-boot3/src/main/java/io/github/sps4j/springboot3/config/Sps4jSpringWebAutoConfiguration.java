package io.github.sps4j.springboot3.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(Sps4jSpringWebProperties.class)
public class Sps4jSpringWebAutoConfiguration {
}
