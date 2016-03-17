package com.closer.department.domain;

import com.closer.common.constant.IDG;
import com.closer.common.domain.BaseTenantDomain;
import com.closer.common.handler.TableProvider;
import com.closer.employee.domain.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 部门实体
 * Created by closer on 2016/1/20.
 */
@Entity
@Table(name = TableProvider.PREFIX_ + "department")
@GenericGenerator(name = "id", strategy = IDG.DISTRIBUTED_IDENTITY)
public class Department extends BaseTenantDomain<Long> {

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
