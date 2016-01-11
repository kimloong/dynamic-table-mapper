package com.closer.company.web;

import com.closer.company.domain.Company;
import com.closer.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "/short_name/{shortName}", method = RequestMethod.GET)
    public Company getByShortName(@PathVariable String shortName) {
        return service.findByShortName(shortName);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Company> list() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Company  add(@RequestBody Company company) {
        return service.save(company);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Company  update(@RequestBody Company company) {
        return service.save(company);
    }
}
