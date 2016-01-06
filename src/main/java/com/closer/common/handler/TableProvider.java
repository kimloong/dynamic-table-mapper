package com.closer.common.handler;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
public class TableProvider {

    private static final String DEFAULT_PREFIX = "default";
    public static final String PREFIX = "#org#";
    public static final String PREFIX_ = PREFIX + "_";
    private static ThreadLocal<String> typeThreadLocal = new ThreadLocal<>();

    public static void setTablePrefix(String type) {
        typeThreadLocal.set(type);
    }

    public static String getTablePrefix() {
        String prefix = typeThreadLocal.get();
        return StringUtils.defaultString(prefix, DEFAULT_PREFIX);
    }
}
