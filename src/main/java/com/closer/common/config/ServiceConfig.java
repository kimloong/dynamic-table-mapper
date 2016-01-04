package com.closer.common.config;

import com.closer.common.handler.TableProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

/**
 * Created by closer on 2016/1/3.
 */
@Configuration
@ComponentScan(basePackages = "com.closer",
        excludeFilters = @ComponentScan.Filter(Controller.class))
public class ServiceConfig {
    @Bean
    public TableProvider tableProvider() {
        return new TableProvider();
    }
}
