package com.closer.employee.service;

import com.closer.common.service.BaseTenantService;
import com.closer.employee.domain.Employee;
import com.closer.employee.job.HappyBirthdayJob;
import com.closer.tenant.service.TenantSupport;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 员工Service
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Service
public class EmployeeService extends BaseTenantService<Employee> implements TenantSupport {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Scheduler scheduler;

    @Override
    public Set<Class> getEntities() {
        Set<Class> set = new HashSet<>();
        set.add(Employee.class);
        return set;
    }

    @Override
    public Employee update(Long id, Map<String, Object> map) {
        Employee employee = super.update(id, map);
        eventPublisher.publishEvent(employee);
        return employee;
    }

    @Override
    public Employee add(Employee employee) {
        employee = super.add(employee);
        eventPublisher.publishEvent(employee);
        return employee;
    }

    @TransactionalEventListener
    public void handleEmployee(Employee employee) throws SchedulerException {
        HappyBirthdayJob.trigger(scheduler, employee);
    }
}
