package com.closer.common.handler;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by closer on 2016/1/27.
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource{

    @Override
    protected Object determineCurrentLookupKey() {
        return TableProvider.getDataSoureName();
    }
}
