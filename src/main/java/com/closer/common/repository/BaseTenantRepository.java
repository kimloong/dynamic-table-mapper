package com.closer.common.repository;

import com.closer.common.domain.BaseTenantDomain;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by closer on 2016/1/27.
 */
@NoRepositoryBean
public interface BaseTenantRepository<T extends BaseTenantDomain> extends BaseRepository<T> {
    T findByIdAndTenant(Long id, long tenant);

    List<T> findByTenant(long tenant);
}
