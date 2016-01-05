package com.closer.common;

import com.closer.common.domain.BaseDoamin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by closer on 2016/1/5.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseDoamin> extends JpaRepository<T,Long>{
}
