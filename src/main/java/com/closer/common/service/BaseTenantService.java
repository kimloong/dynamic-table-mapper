package com.closer.common.service;

import com.closer.common.domain.BaseTenantDomain;
import com.closer.common.handler.TableProvider;
import com.closer.common.repository.BaseTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by closer on 2016/1/27.
 */
public abstract class BaseTenantService<T extends BaseTenantDomain> extends BaseService<T> {

    @Autowired
    private BaseTenantRepository<T> baseTenantRepository;

    @Override
    public T findOne(Long id) {
        return baseTenantRepository.findByIdAndTenant(id, TableProvider.getTenantId());
    }

    @Override
    public List<T> findAll() {
        return baseTenantRepository.findByTenant(TableProvider.getTenantId());
    }

    @Override
    public T add(T t) {
        t.setTenant(TableProvider.getTenantId());
        return super.add(t);
    }

    @Override
    public boolean exists(Long id) {
        return findOne(id) != null;
    }
}
