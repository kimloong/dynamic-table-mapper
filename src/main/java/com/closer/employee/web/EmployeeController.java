package com.closer.employee.web;

import com.closer.employee.domain.Employee;
import com.closer.common.handler.TableProvider;
import com.closer.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Employee> list(@RequestParam String name) {
        TableProvider.setType(name);
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Employee save(@RequestBody Employee employee) {
        TableProvider.setType(employee.getName());
        return service.save(employee);
    }
}
