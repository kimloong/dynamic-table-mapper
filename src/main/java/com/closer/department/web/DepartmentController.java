package com.closer.department.web;

import com.closer.department.domain.Department;
import com.closer.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门Controller
 * Created by closer on 2016/1/20.
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Department get(@PathVariable long id) {
        return service.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Department> list() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Department add(@RequestBody Department department) {
        return service.add(department);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Department update(@PathVariable long id,
                             @RequestBody Map<String, Object> departmentMap) {
        return service.update(id, departmentMap);
    }


}
