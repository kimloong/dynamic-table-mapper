package com.closer.company.event;

import com.closer.company.domain.Company;

/**
 * 公司新增事件
 * Created by closer on 2016/1/5.
 */
public class CompanyCreateEvent {
    private Company company;

    public CompanyCreateEvent(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }
}
