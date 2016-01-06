package com.closer.company.domain;

import com.closer.common.domain.BaseDomain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公司实体
 * Created by closer on 2016/1/5.
 */
@Entity
@Table(name = "company")
public class Company extends BaseDomain {

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
