package com.closer.company.service;

import com.closer.common.service.BaseService;
import com.closer.company.domain.Company;
import com.closer.company.event.CompanyCreateEvent;
import com.closer.company.repository.CompanyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 公司Service
 * Created by closer on 2016/1/5.
 */
@Service
@CacheConfig(cacheNames = "companies")
public class CompanyService extends BaseService<Company> {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CompanyRepository repository;

    @Override
    @Cacheable
    public Company findOne(Long id) {
        return super.findOne(id);
    }

    @Override
    @CachePut
    public Company add(Company company) {
        check(company);
        publisher.publishEvent(new CompanyCreateEvent(company));
        return super.add(company);
    }

    @Override
    @CacheEvict
    public Company update(Company company) {
        check(company);
        return super.update(company);
    }

    @Override
    @CacheEvict(key="#p0")
    public Company update(Long id, Map<String, Object> map) {
        return super.update(id, map);
    }

    private void check(Company company) {
        if (StringUtils.isBlank(company.getName())) {
            throw new RuntimeException("公司名不能为空");
        }
        if (StringUtils.isBlank(company.getShortName())) {
            throw new RuntimeException("公司简称不能为空");
        }
    }

    public Company findByShortName(String shortName) {
        return repository.findByShortName(shortName);
    }
}
