package com.github.sps4j.springboot2.context;

import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

/**
 * A singleton holder for the base (host) application's {@link ServletWebServerApplicationContext}.
 * <p>
 * This allows plugins to access the base application's context, for instance, to autowire beans
 * from the base application into plugin components.
 *
 * @author Allan-QLB
 */
public final class BaseApplicationContextHolder {

    private static BaseApplicationContextHolder instance;
    private final ServletWebServerApplicationContext baseAppContext;


    private BaseApplicationContextHolder(ServletWebServerApplicationContext baseAppContext) {
        this.baseAppContext = baseAppContext;
    }

    /**
     * Creates and initializes the singleton instance.
     * This method is synchronized to ensure thread safety.
     *
     * @param applicationContext The base application's context.
     * @return The singleton instance.
     */
    public static synchronized BaseApplicationContextHolder create(ServletWebServerApplicationContext applicationContext) {
        if (instance == null) {
            instance = new BaseApplicationContextHolder(applicationContext);
        }
        return instance;
    }

    /**
     * Gets the singleton instance.
     *
     * @return The singleton instance.
     * @throws IllegalStateException if the holder has not been initialized via {@link #create(ServletWebServerApplicationContext)}.
     */
    public static BaseApplicationContextHolder getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BaseApplicationContextHolder is not initialized");
        }
        return instance;
    }

    /**
     * Gets the base application's {@link ServletWebServerApplicationContext}.
     *
     * @return The base application context.
     */
    public static ServletWebServerApplicationContext getBaseAppContext() {
        return getInstance().baseAppContext;
    }

    /**
     * Autowires dependencies into the given object from the base application's bean factory.
     *
     * @param obj The object to autowire.
     */
    public static void autowireFromBase(Object obj) {
        getInstance().baseAppContext.getAutowireCapableBeanFactory().autowireBean(obj);
    }


}

