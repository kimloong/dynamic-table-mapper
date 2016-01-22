package com.closer.employee.domain;


import com.closer.common.domain.BaseDomain;
import com.closer.common.handler.TableProvider;
import com.closer.department.domain.Department;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;

/**
 * 员工实体
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Entity
@Table(name = TableProvider.PREFIX_ + "employee")
public class Employee extends BaseDomain {

    public interface List {
    }

    public interface Detail {
    }

    private String name;

    private String enName;

    @JsonView({Detail.class})
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Department department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
