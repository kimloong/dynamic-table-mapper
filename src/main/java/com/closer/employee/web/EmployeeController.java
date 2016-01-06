package com.closer.employee.web;

import com.closer.common.handler.TableProvider;
import com.closer.company.domain.Company;
import com.closer.company.service.CompanyService;
import com.closer.employee.domain.Employee;
import com.closer.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工控制器
 * Created by closer on 2016/1/4.
 */
@RestController
@RequestMapping("/companies/{companyId}/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> list(@PathVariable long companyId) {
        Company company = companyService.findOne(companyId);
        TableProvider.setTablePrefix(company.getShortName());
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Employee save(@PathVariable long companyId, @RequestBody Employee employee) {
        Company company = companyService.findOne(companyId);
        TableProvider.setTablePrefix(company.getShortName());
        return service.save(employee);
    }
}
