package com.closer.company.service;

import com.closer.common.service.BaseTenantService;
import com.closer.company.domain.Company;
import com.closer.tenant.service.TenantSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 公司Service
 * Created by closer on 2016/1/5.
 */
@Service
public class CompanyService extends BaseTenantService<Company> implements TenantSupport {

    @Override
    public Company add(Company company) {
        check(company);
        return super.add(company);
    }

    @Override
    public Company update(Company company) {
        check(company);
        return super.update(company);
    }

    private void check(Company company) {
        if (StringUtils.isBlank(company.getName())) {
            throw new RuntimeException("公司名不能为空");
        }
        if (StringUtils.isBlank(company.getShortName())) {
            throw new RuntimeException("公司简称不能为空");
        }
    }

    @Override
    public Set<Class> getEntities() {
        Set<Class> set = new HashSet<>();
        set.add(Company.class);
        return set;
    }
}
