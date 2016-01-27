package com.closer.tenant.domain;

import com.closer.common.domain.BaseDomain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by closer on 2016/1/27.
 */
@Entity
@Table(name = "tenant")
public class Tenant extends BaseDomain {

    private String name;

    private String dataSourceName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
