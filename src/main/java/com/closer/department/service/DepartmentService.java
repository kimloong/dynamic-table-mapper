package com.closer.department.service;

import com.closer.common.helper.TableHelper;
import com.closer.common.service.BaseService;
import com.closer.company.event.CompanyCreateEvent;
import com.closer.department.domain.Department;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * Created by closer on 2016/1/20.
 */
@Service
public class DepartmentService extends BaseService<Department> {

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handleCompanyCreate(CompanyCreateEvent event) {
        TableHelper.addEntityClass(Department.class);
    }

    @Override
    @Cacheable("departments")
    public Department findOne(Long id) {
        return super.findOne(id);
    }
}
