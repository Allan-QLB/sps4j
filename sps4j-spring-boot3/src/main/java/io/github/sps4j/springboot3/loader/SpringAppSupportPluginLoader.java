package io.github.sps4j.springboot3.loader;


import io.github.sps4j.common.meta.MetaInfo;
import io.github.sps4j.common.utils.CallUtils;
import io.github.sps4j.core.Sps4jPlugin;
import io.github.sps4j.core.load.DefaultPluginLoader;
import io.github.sps4j.springboot3.SpringBoot3AppPlugin;
import io.github.sps4j.springboot3.context.HostApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

@Slf4j
public class SpringAppSupportPluginLoader extends DefaultPluginLoader {


    @Override
    public Sps4jPlugin pluginCreated(Sps4jPlugin pluginInstance, MetaInfo metadata) {
        if (!(pluginInstance instanceof SpringBoot3AppPlugin)) {
            try {
                CallUtils.runWithContextLoader(pluginInstance.getClass().getClassLoader(), () -> HostApplicationContextHolder.autowireFromHost(pluginInstance));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return pluginInstance;
    }

    @Override
    public Sps4jPlugin postLoadPlugin(Sps4jPlugin pluginInstance, MetaInfo metadata) {
        if (pluginInstance instanceof SpringBoot3AppPlugin) {
            final String shortName = ClassUtils.getShortName(metadata.getDescriptor().getClassName());
            return (Sps4jPlugin) ((SpringBoot3AppPlugin) pluginInstance).getApplicationContext()
                .getBean(Introspector.decapitalize(shortName));
        }
        return pluginInstance;
    }
}
