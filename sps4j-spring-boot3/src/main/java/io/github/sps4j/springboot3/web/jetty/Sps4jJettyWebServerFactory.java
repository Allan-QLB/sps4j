package io.github.sps4j.springboot3.web.jetty;

import io.github.sps4j.core.load.Sps4jPluginClassLoader;
import io.github.sps4j.springboot3.context.HostApplicationContextHolder;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;

public class Sps4jJettyWebServerFactory extends JettyServletWebServerFactory {

    @Override
    @SneakyThrows
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        if (Thread.currentThread().getContextClassLoader() instanceof Sps4jPluginClassLoader) {
            ApplicationContext baseApplicationContext = HostApplicationContextHolder.getHostAppContext();
            Assert.isInstanceOf(ServletWebServerApplicationContext.class, baseApplicationContext);
            ServletWebServerApplicationContext webServerApplicationContext = (ServletWebServerApplicationContext) baseApplicationContext;
            JettyWebServer webServer = (JettyWebServer) webServerApplicationContext.getWebServer();
            Server jetty = webServer.getServer();
            WebAppContext context = new WebAppContext();
            configurePluginWebAppContext(context, initializers);
            ContextHandlerCollection handler = (ContextHandlerCollection) jetty.getHandler();
            handler.addHandler(context);
            this.logger.info("Server initialized");
            return new Sps4jJettyWebServer(jetty, context, handler);
        }
        else {
            return super.getWebServer(initializers);
        }
    }

    private void configurePluginWebAppContext(WebAppContext context, ServletContextInitializer... initializers) {
        Assert.notNull(context, "Context must not be null");
        context.setTempDirectory(new File("jetty-docbase"));
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        String contextPath = getContextPath();
        context.setContextPath(StringUtils.hasLength(contextPath) ? contextPath : "/");
        context.setDisplayName(getDisplayName());
        File documentRoot = getValidDocumentRoot();
        if (documentRoot != null) {
            context.setBaseResource(ResourceFactory.root().newResource(documentRoot.toURI()));
        } else {
            context.setBaseResource(ResourceFactory.root().newResource(createTempDir("jetty-docbase").toURI()));
        }
        ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
        org.eclipse.jetty.ee10.webapp.Configuration[] configurations = getWebAppContextConfigurations(context, initializersToUse);
        context.setConfigurations(configurations);
        postProcessWebAppContext(context);
    }

    @Override
    protected JettyWebServer getJettyWebServer(Server server) {
        Handler handler = server.getHandler();
        if (!(handler instanceof ContextHandlerCollection)) {
            ContextHandlerCollection hc = new ContextHandlerCollection();
            hc.addHandler(handler);
            server.setHandler(hc);
        }
        return super.getJettyWebServer(server);
    }
}
