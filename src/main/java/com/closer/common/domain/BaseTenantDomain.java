package com.closer.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by closer on 2016/1/27.
 */
@MappedSuperclass
public abstract class BaseTenantDomain extends BaseDomain {

    @Column(updatable = false)
    @JsonIgnore
    private Long tenant;

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }
}
