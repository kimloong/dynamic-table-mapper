package com.closer.company.service;

import com.closer.common.service.BaseService;
import com.closer.company.domain.Company;
import com.closer.company.event.CompanyCreateEvent;
import com.closer.company.repository.CompanyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 公司Service
 * Created by closer on 2016/1/5.
 */
@Service
public class CompanyService extends BaseService<Company> {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CompanyRepository repository;

    @Override
    public Company save(Company company) {
        check(company);
        publisher.publishEvent(new CompanyCreateEvent(company));
        return super.save(company);
    }

    private void check(Company company) {
        if (StringUtils.isBlank(company.getName())) {
            throw new RuntimeException("公司名不能为空");
        }
        if (StringUtils.isBlank(company.getShortName())) {
            throw new RuntimeException("公司简称不能为空");
        }
        if (!StringUtils.isAlpha(company.getShortName())) {
            throw new RuntimeException("简称必须全部为英文");
        }
    }

    public Company findByShortName(String shortName) {
        return repository.findByShortName(shortName);
    }
}
