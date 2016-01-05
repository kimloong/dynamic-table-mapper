package com.closer.employee.domain;


import com.closer.common.domain.BaseDoamin;
import com.closer.company.domain.Company;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 员工实体
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Entity
@Table(name = "#org#_employee")
public class Employee extends BaseDoamin {

    private String name;

    @ManyToOne
    private Company company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
