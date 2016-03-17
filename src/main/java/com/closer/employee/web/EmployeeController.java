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
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @JsonView(View.EagerDetail.class)
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Employee get(@PathVariable("id") long id) {
        return service.findOne(id);
    }

    @JsonView(View.List.class)
    @RequestMapping(method = RequestMethod.GET)
    public List list() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Employee add(@RequestBody Employee employee) {
        return service.add(employee);
    }
}
