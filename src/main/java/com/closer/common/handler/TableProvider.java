package com.closer.common.handler;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
public class TableProvider {

    public static final String DEFAULT_COLLECTION_NAME = "default";
    private static ThreadLocal<String> typeThreadLocal = new ThreadLocal<>();

    public static void setType(String type) {
        typeThreadLocal.set(type);
    }

    public static String getTablePrefix() {
        String type = typeThreadLocal.get();
        if (StringUtils.isNotBlank(type)) {
            String collectionName = doMapper(type);
            return collectionName;
        } else {
            return DEFAULT_COLLECTION_NAME;
        }
    }

    private static String doMapper(String type) {
        if (StringUtils.equals(type, "aa")) {
            return "aa";
        }else {
            return "bb";
        }
    }
}
