package com.closer.department.web;

import com.closer.common.handler.TableProvider;
import com.closer.company.domain.Company;
import com.closer.company.service.CompanyService;
import com.closer.department.domain.Department;
import com.closer.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门Controller
 * Created by closer on 2016/1/20.
 */
@RestController
@RequestMapping("/companies/{companyId}/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/{departmentId}", method = RequestMethod.GET)
    public Department get(@PathVariable("companyId") long companyId,
                          @PathVariable("departmentId") long departmentId) {
        Company company = companyService.findOne(companyId);
        TableProvider.setTablePrefix(company.getShortName());
        return service.findOne(departmentId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Department> list(@PathVariable long companyId) {
        Company company = companyService.findOne(companyId);
        TableProvider.setTablePrefix(company.getShortName());
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Department add(@PathVariable long companyId, @RequestBody Department department) {
        Company company = companyService.findOne(companyId);
        TableProvider.setTablePrefix(company.getShortName());
        return service.add(department);
    }
}
