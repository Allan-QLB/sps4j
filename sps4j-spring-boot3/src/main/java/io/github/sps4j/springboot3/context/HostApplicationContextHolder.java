package io.github.sps4j.springboot3.context;

import org.springframework.context.ApplicationContext;

public final class HostApplicationContextHolder {

    private static HostApplicationContextHolder instance;
    private final ApplicationContext hostApplicationContext;


    private HostApplicationContextHolder(ApplicationContext hostApplicationContext) {
        this.hostApplicationContext = hostApplicationContext;
    }

    public static synchronized HostApplicationContextHolder create(ApplicationContext applicationContext) {
        if (instance == null) {
            instance = new HostApplicationContextHolder(applicationContext);
        }
        return instance;
    }

    public static HostApplicationContextHolder getInstance() {
        if (instance == null) {
            throw new IllegalStateException("HostApplicationContextHolder is not initialized");
        }
        return instance;
    }

    public static ApplicationContext getHostAppContext() {
        return getInstance().hostApplicationContext;
    }

    public static void autowireFromHost(Object obj) {
        getInstance().hostApplicationContext.getAutowireCapableBeanFactory().autowireBean(obj);
    }


}
