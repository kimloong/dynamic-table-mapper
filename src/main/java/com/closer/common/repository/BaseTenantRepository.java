package com.closer.common.repository;

import com.closer.common.domain.BaseTenantDomain;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 租户业务基础Dao
 * Created by closer on 2016/1/27.
 */
@NoRepositoryBean
public interface BaseTenantRepository<T extends BaseTenantDomain> extends BaseRepository<T> {

    T findByIdAndTenant(Long id, long tenant);

    List<T> findByTenant(long tenant);
}
