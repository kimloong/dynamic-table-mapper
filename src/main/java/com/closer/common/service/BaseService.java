package com.closer.common.service;

import com.closer.common.domain.BaseDomain;
import com.closer.common.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

/**
 * BaseService
 * Created by closer on 2016/1/5.
 */
@Transactional
public class BaseService<T extends BaseDomain> {

    @Autowired
    private BaseRepository<T> repository;

    public T findOne(Long id) {
        return repository.findOne(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T save(T t) {
        return repository.save(t);
    }

    public void delete(T t) {
        repository.delete(t);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public boolean exists(Long id) {
        return repository.exists(id);
    }

}
