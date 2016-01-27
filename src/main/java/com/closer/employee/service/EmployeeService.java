package com.closer.employee.service;

import com.closer.common.service.BaseTenantService;
import com.closer.employee.domain.Employee;
import com.closer.tenant.service.TenantSupport;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 员工Service
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Service
public class EmployeeService extends BaseTenantService<Employee> implements TenantSupport {

    @Override
    public Set<Class> getEntities() {
        Set<Class> set = new HashSet<>();
        set.add(Employee.class);
        return set;
    }

}
