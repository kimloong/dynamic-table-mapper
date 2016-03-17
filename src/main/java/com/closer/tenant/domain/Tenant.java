package com.closer.tenant.domain;

import com.closer.common.constant.IDG;
import com.closer.common.domain.BaseDomain;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by closer on 2016/1/27.
 */
@Entity
@Table(name = "t_tenant")
@GenericGenerator(name = "id", strategy = IDG.IDENTITY)
public class Tenant extends BaseDomain<Long> {

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
