package io.github.sps4j.springboot2.webflux;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.server.reactive.HttpHandler;

@AllArgsConstructor
public class Sps4jNettyWebServer implements WebServer  {
    private final NettyWebServer hostWebServer;
    private final Sps4jContextPathCompositeHandler hostHandler;
    private final HttpHandler handler;
    private final WebFluxProperties webFluxProperties;

    @Override
    public void start() throws WebServerException {
        hostHandler.getHandlerMap().put(webFluxProperties.getBasePath(), handler);
    }

    @Override
    public void stop() throws WebServerException {
        hostHandler.getHandlerMap().remove(webFluxProperties.getBasePath());
    }

    @Override
    public int getPort() {
        return hostWebServer.getPort();
    }
}