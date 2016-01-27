package com.closer.tenant.event;

import com.closer.tenant.domain.Tenant;

/**
 * 公司新增事件
 * Created by closer on 2016/1/5.
 */
public class TenantCreateEvent {
    private Tenant tenant;

    public TenantCreateEvent(Tenant tenant) {
        this.tenant = tenant;
    }

    public Tenant getTenant() {
        return tenant;
    }
}
