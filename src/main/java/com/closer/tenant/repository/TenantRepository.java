package com.closer.tenant.repository;

import com.closer.common.repository.BaseRepository;
import com.closer.tenant.domain.Tenant;
import org.springframework.stereotype.Repository;

/**
 * Created by closer on 2016/1/27.
 */
@Repository
public interface TenantRepository extends BaseRepository<Tenant, Long> {
}
