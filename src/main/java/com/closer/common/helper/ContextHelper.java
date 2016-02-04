package com.closer.common.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by closer on 2016/2/4.
 */
public class ContextHelper implements ApplicationContextAware {

    private static ApplicationContext _applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        _applicationContext = applicationContext;
    }

    public static ApplicationContext applicationContext() {
        return _applicationContext;
    }
}
