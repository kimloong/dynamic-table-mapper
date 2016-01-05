package com.closer.common.handler;

import org.hibernate.cfg.DefaultNamingStrategy;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
public class TableNamingStrategy extends DefaultNamingStrategy {

    @Override
    public String classToTableName(String className) {
        return super.classToTableName(className).replace("#org#", TableProvider.getTablePrefix());
    }

    @Override
    public String tableName(String tableName) {
        return super.tableName(tableName).replace("#org#", TableProvider.getTablePrefix());
    }
}
