package com.closer.common.handler;

import org.hibernate.cfg.DefaultNamingStrategy;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
public class TableNamingStrategy extends DefaultNamingStrategy {

    @Override
    public String classToTableName(String className) {
        return TableProvider.getTablePrefix() + "_" + super.classToTableName(className);
    }

    @Override
    public String tableName(String tableName) {
        return TableProvider.getTablePrefix() + "_" + super.tableName(tableName);
    }
}
