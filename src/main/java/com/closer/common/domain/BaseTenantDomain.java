package com.closer.common.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by closer on 2016/1/27.
 */
@MappedSuperclass
public abstract class BaseTenantDomain<I extends Serializable> extends BaseDomain<I> {

    @Column(updatable = false)
    private Long tenant;

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }
}
