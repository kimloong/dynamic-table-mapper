package com.closer.company.repository;

import com.closer.common.repository.BaseTenantRepository;
import com.closer.company.domain.Company;
import org.springframework.stereotype.Repository;

/**
 * 公司Dao
 * Created by closer on 2016/1/5.
 */
@Repository
public interface CompanyRepository extends BaseTenantRepository<Company> {
}
