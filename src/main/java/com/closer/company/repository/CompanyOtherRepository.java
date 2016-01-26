package com.closer.company.repository;

import com.closer.company.domain.Company;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/26.
 */
public interface CompanyOtherRepository {

    Company findOne(Long id);

    <S extends Company> S save(S s);
}
