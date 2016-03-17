package com.closer.common.repository;

import com.closer.common.domain.BaseDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 基础Repository
 * Created by closer on 2016/1/5.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseDomain<I>,I extends Serializable> extends JpaRepository<T,I>{
}
