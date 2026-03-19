package io.github.sps4j.springboot2.webflux;

import io.github.sps4j.common.meta.MetaInfo;
import io.github.sps4j.springboot2.config.Sps4jSpringWebProperties;
import io.github.sps4j.springboot2.context.HostApplicationContextHolder;
import io.github.sps4j.springboot2.context.PluginSpringbootBootstrapContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.boot.web.reactive.server.AbstractReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;

import org.springframework.context.ApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;


@AllArgsConstructor
public class Sps4jNettyWebServerFactory extends AbstractReactiveWebServerFactory {

    private final WebFluxProperties webFluxProperties;
    private final ApplicationContext applicationContext;

    @SneakyThrows
    @Override
    public WebServer getWebServer(HttpHandler httpHandler) {
        WebServerApplicationContext hostAppContext = (WebServerApplicationContext) HostApplicationContextHolder.getHostAppContext();
        Sps4jContextPathCompositeHandler hostHandler =
                (Sps4jContextPathCompositeHandler) hostAppContext.getBean(HttpHandler.class);
        MetaInfo metaInfo = PluginSpringbootBootstrapContext.getCurrentPluginMetaInfo();
        String prefix = "";
        if (applicationContext.getBean(Sps4jSpringWebProperties.class).getWebFlux().isAddContextPrefix()) {
            prefix = "/"
                    + metaInfo.getDescriptor().getType()
                    + "/"
                    + metaInfo.getDescriptor().getName();
        }
        return new Sps4jNettyWebServer((NettyWebServer) hostAppContext.getWebServer(),
                hostHandler, httpHandler, webFluxProperties, prefix);
    }
}
