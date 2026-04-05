package io.github.sps4j.springboot3;


import io.github.sps4j.common.meta.MetaInfo;
import io.github.sps4j.core.Sps4jPlugin;
import io.github.sps4j.core.load.Sps4jPluginClassLoader;
import io.github.sps4j.springboot3.context.PluginSpringbootBootstrapContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public abstract class SpringBoot3AppPlugin implements Sps4jPlugin {

    public static final String TAG_SPRING_MVC = "spring-webmvc";
    public static final String TAG_SPRING_WEBFLUX = "spring-webflux";
    public static final String TAG_SPRING_NOWEB = "spring-noweb";
    public static final String SPRING_CONFIG_FILE_NAME_PATTERN = "^(application|bootstrap)(-[a-zA-Z0-9_]+)?\\.(properties|ya?ml|xml)$";

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void onLoad(Map<String, Object> conf, MetaInfo metadata) {
        Sps4jPluginClassLoader classLoader = (Sps4jPluginClassLoader) getClass().getClassLoader();
        classLoader.addIgnoreParentResourceNamePattern(SPRING_CONFIG_FILE_NAME_PATTERN);
        final SpringApplication springApplication = new SpringApplication(this.getClass());
        List<String> tags = metadata.getDescriptor().getTags();
        if (tags.contains(TAG_SPRING_MVC)) {
            springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        } else if (tags.contains(TAG_SPRING_WEBFLUX)) {
            springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
        } else if (tags.contains(TAG_SPRING_NOWEB)) {
            springApplication.setWebApplicationType(WebApplicationType.NONE);
        }
        PluginSpringbootBootstrapContext.setCurrentPluginMetaInfo(metadata);
        try {
            applicationContext = springApplication.run();
            ((Sps4jPluginClassLoader) this.getClass().getClassLoader()).addOnCloseAction(() -> {
                if (springApplication.getWebApplicationType() == WebApplicationType.REACTIVE) {
                    new Thread(() -> stopPluginApplicationContext(applicationContext)).start();
                } else {
                    stopPluginApplicationContext(applicationContext);
                }
            });
        } finally {
            PluginSpringbootBootstrapContext.removeCurrentPluginMetaInfo();
        }

    }

    private static void stopPluginApplicationContext(ConfigurableApplicationContext applicationContext) {
        log.info("Stop plugin application context {}", applicationContext.getApplicationName());
        applicationContext.close();
    }

}
