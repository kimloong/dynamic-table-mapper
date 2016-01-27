package com.closer.tenant.repository.impl;

import com.closer.tenant.domain.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * Created by closer on 2016/1/27.
 */
@Repository
@CacheConfig(cacheNames = "tenants")
public class TenantRepositoryImpl extends SimpleJpaRepository<Tenant, Long> {

    @Autowired
    public TenantRepositoryImpl(EntityManager entityManager) {
        super(Tenant.class, entityManager);
    }

    @Cacheable
    @Override
    public Tenant findOne(Long id) {
        return super.findOne(id);
    }

    @CacheEvict
    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @CacheEvict(key = "#p0.id")
    @Override
    public Tenant save(Tenant entity) {
        return super.save(entity);
    }
}
