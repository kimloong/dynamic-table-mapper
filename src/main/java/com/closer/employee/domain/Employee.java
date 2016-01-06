package com.closer.employee.domain;


import com.closer.common.domain.BaseDomain;
import com.closer.common.handler.TableProvider;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 员工实体
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Entity
@Table(name = TableProvider.PREFIX_ + "employee")
public class Employee extends BaseDomain {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
