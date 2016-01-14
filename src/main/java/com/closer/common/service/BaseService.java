package com.closer.common.service;

import com.closer.common.domain.BaseDomain;
import com.closer.common.repository.BaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * BaseService
 * Created by closer on 2016/1/5.
 */
@Transactional
public class BaseService<T extends BaseDomain> {

    @Autowired
    private BaseRepository<T> repository;

    public static final ObjectMapper om = new ObjectMapper();
    static {
        om.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    public T findOne(Long id) {
        return repository.findOne(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T save(T t) {
        return repository.save(t);
    }

    public T updateByJson(Long id,String json) {
        T t = findOne(id);
        if (t == null) {
            throw new RuntimeException("找不到相关对象");
        }
        try {
            om.readerForUpdating(t).readValue(json);

        } catch (IOException e) {
            throw new RuntimeException();
        }
        return save(t);
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
