package com.closer.company.web;

import com.closer.company.domain.Company;
import com.closer.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 公司控制器
 * Created by closer on 2016/1/5.
 */
@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Company get(@PathVariable Long id) {
        return service.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Company> list() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Company  add(@RequestBody Company company) {
        return service.add(company);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Company  update(@RequestBody Company company) {
        return service.update(company);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Company  update(@RequestBody Map<String,Object> map,@PathVariable Long id) {
        return service.update(id, map);
    }
}
