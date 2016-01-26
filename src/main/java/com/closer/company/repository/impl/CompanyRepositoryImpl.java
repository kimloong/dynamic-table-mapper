package com.closer.company.repository.impl;

import com.closer.company.domain.Company;
import com.closer.company.repository.CompanyOtherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/26.
 */
@Repository
@CacheConfig(cacheNames = "companies")
public class CompanyRepositoryImpl extends SimpleJpaRepository<Company, Long> implements CompanyOtherRepository {

    @Autowired
    public CompanyRepositoryImpl(EntityManager entityManager) {
        super(Company.class, entityManager);
    }

    @Cacheable
    @Override
    public Company findOne(Long id) {
        return super.findOne(id);
    }

    @Override
    @CacheEvict(key="#p0.id")
    public <S extends Company> S save(S s) {
        return super.save(s);
    }

}
