package com.closer.tenant.service;

import com.closer.common.service.BaseService;
import com.closer.tenant.domain.Tenant;
import com.closer.tenant.event.TenantCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Created by closer on 2016/1/27.
 */
@Service
public class TenantService extends BaseService<Tenant> {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public Tenant add(Tenant tenant) {
        super.add(tenant);
        publisher.publishEvent(new TenantCreateEvent(tenant));
        return tenant;
    }
}
