package com.github.sps4j.core.test;

import com.github.sps4j.annotation.Sps4jPluginInterface;
import com.github.sps4j.core.Sps4jPlugin;

@Sps4jPluginInterface("test")
public interface TestPlugin extends Sps4jPlugin {
    String test();
}
