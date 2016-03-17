package com.closer.department.service;

import com.closer.common.service.BaseTenantService;
import com.closer.department.domain.Department;
import com.closer.tenant.service.TenantSupport;
import org.springframework.stereotype.Service;

/**
 * Created by closer on 2016/1/20.
 */
@Service
public class DepartmentService extends BaseTenantService<Department, Long> implements TenantSupport {


    @Override
    public Class[] getEntities() {
        return new Class[]{Department.class};
    }
}
