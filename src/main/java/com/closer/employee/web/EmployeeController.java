package com.closer.employee.web;

import com.closer.common.view.View;
import com.closer.employee.domain.Employee;
import com.closer.employee.service.EmployeeService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工控制器
 * Created by closer on 2016/1/4.
 */
@RestController
@RequestMapping("/companies")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @JsonView(View.EagerDetail.class)
    @RequestMapping(value = "/employees/{employeeId}",method = RequestMethod.GET)
    public Employee get(@PathVariable("employeeId") long employeeId) {
        return service.findOne(employeeId);
    }

    @JsonView(View.List.class)
    @RequestMapping(value = "{companyId}/employees",method = RequestMethod.GET)
    public List list(@PathVariable long companyId) {
        return service.findAll();
    }

    @RequestMapping(value = "/employees",method = RequestMethod.POST)
    public Employee add(@PathVariable long companyId, @RequestBody Employee employee) {
        return service.add(employee);
    }
}
