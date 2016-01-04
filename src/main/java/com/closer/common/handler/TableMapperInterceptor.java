package com.closer.common.handler;

import org.hibernate.EmptyInterceptor;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
public class TableMapperInterceptor extends EmptyInterceptor{

    @Override
    public String onPrepareStatement(String sql) {
        return sql.replace("#org#",TableProvider.getTablePrefix());
    }
}
