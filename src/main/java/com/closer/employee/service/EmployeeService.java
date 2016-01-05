package com.closer.employee.service;

import com.closer.common.service.BaseService;
import com.closer.company.domain.Company;
import com.closer.company.event.CompanyCreateEvent;
import com.closer.employee.domain.Employee;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * 员工Service
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Service
public class EmployeeService extends BaseService<Employee> {

    @EventListener
    public void handleCompanyCreate(CompanyCreateEvent event){

    }
}
