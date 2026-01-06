package com.github.sps4j.example.springmvc.plugin;


import com.github.sps4j.annotation.Sps4jPlugin;
import com.github.sps4j.core.test.TestPlugin;
import com.github.sps4j.example.springmvc.plugin.service.TestService;
import com.github.sps4j.springboot2.SpringBoot2AppPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Sps4jPlugin(version = "1.2.1", name = "example",
        productVersionConstraint = ">=1.0.0",
        tags = {SpringBoot2AppPlugin.TAG_SPRING_MVC}
)
@SpringBootApplication
public class AppPlugin extends SpringBoot2AppPlugin implements TestPlugin {

    @Autowired
    private TestService testService;
    @Override
    public String test() {
        testService.test();
        return "ok";
    }
}
