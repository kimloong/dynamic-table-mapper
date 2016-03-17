package com.closer.common.repository;

import com.closer.common.domain.BaseTenantDomain;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * 租户业务基础Dao
 * Created by closer on 2016/1/27.
 */
@NoRepositoryBean
public interface BaseTenantRepository<T extends BaseTenantDomain<I>,I extends Serializable> extends BaseRepository<T,I> {

    T findByIdAndTenant(I id, long tenant);

    List<T> findByTenant(long tenant);
}
