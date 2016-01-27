package com.closer.department.service;

import com.closer.common.service.BaseTenantService;
import com.closer.department.domain.Department;
import com.closer.tenant.service.TenantSupport;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by closer on 2016/1/20.
 */
@Service
public class DepartmentService extends BaseTenantService<Department> implements TenantSupport {


    @Override
    public Set<Class> getEntities() {
        Set<Class> set = new HashSet<>();
        set.add(Department.class);
        return set;
    }
}
