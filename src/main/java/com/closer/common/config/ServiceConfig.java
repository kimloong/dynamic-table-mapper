package com.closer.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Created by closer on 2016/1/3.
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@ComponentScan(basePackages = "com.closer",
        excludeFilters = @ComponentScan.Filter({Controller.class, Configuration.class,
                Repository.class, ControllerAdvice.class}))
public class ServiceConfig {

}
