package com.closer.tenant.web;

import com.closer.tenant.domain.Tenant;
import com.closer.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by closer on 2016/1/27.
 */
@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @RequestMapping(method = RequestMethod.POST)
    public Tenant add(@RequestBody Tenant tenant) {
        return tenantService.add(tenant);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Tenant add(@PathVariable long id) {
        return tenantService.findStrictOne(id);
    }
}
