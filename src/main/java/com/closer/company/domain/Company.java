package com.closer.company.domain;

import com.closer.common.domain.BaseTenantDomain;
import com.closer.common.handler.TableProvider;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 公司实体
 * Created by closer on 2016/1/5.
 */
@Entity
@Table(name = TableProvider.PREFIX_ + "company",
        indexes = {@Index(unique = true, columnList = "shortName")})
public class Company extends BaseTenantDomain {

    private String name;
    private String shortName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
