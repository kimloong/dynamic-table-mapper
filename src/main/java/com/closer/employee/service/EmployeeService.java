package com.closer.employee.service;

import com.closer.common.helper.TableHelper;
import com.closer.common.service.BaseService;
import com.closer.company.event.CompanyCreateEvent;
import com.closer.employee.domain.Employee;
import org.hibernate.Hibernate;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 员工Service
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Service
public class EmployeeService extends BaseService<Employee> {

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handleCompanyCreate(CompanyCreateEvent event) {
        TableHelper.addEntityClass(Employee.class);
    }

    @Override
    public Employee findOne(Long id) {
        Employee employee = super.findOne(id);
        Hibernate.initialize(employee.getDepartment());
        return employee;
    }
}
