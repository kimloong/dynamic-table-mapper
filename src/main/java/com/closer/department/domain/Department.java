package com.closer.department.domain;

import com.closer.common.domain.BaseDomain;
import com.closer.common.handler.TableProvider;
import com.closer.employee.domain.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * 部门实体
 * Created by closer on 2016/1/20.
 */
@Entity
@Table(name = TableProvider.PREFIX_ + "department")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department extends BaseDomain {

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.DETACH)
    private List<Employee> employees;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("department")
    private Employee manager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
