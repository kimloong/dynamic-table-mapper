package com.closer.department.domain;

import com.closer.common.domain.BaseDomain;
import com.closer.common.handler.TableProvider;
import com.closer.employee.domain.Employee;

import javax.persistence.*;
import java.util.List;

/**
 * Created by closer on 2016/1/20.
 */
@Entity
@Table(name = TableProvider.PREFIX_ + "department")
public class Department extends BaseDomain {

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.DETACH)
    private List<Employee> employees;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
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
