package io.github.sps4j.springboot3.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sps4j")
@Getter
@Setter
@ToString
public class Sps4jSpringWebProperties {

    private Mvc springMvc = new Mvc();
    private WebFlux webFlux = new WebFlux();

    @Getter
    @Setter
    @ToString
    public static class Mvc {
        private boolean addServletContextPrefix = true;
    }

    @Getter
    @Setter
    @ToString
    public static class WebFlux {
        private boolean addContextPrefix = true;
    }

}
