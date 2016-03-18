package com.closer.common.service;

import com.closer.common.constant.IDG;
import com.closer.common.domain.BaseDomain;
import com.closer.common.repository.BaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseService
 * Created by closer on 2016/1/5.
 */
@Transactional
public class BaseService<T extends BaseDomain<I>, I extends Serializable> {

    @Autowired
    private BaseRepository<T, I> repository;

    @Autowired
    private ObjectMapper objectMapper;

    public static final Converter<String, String> CONVERTER
            = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

    @Transactional(propagation = Propagation.SUPPORTS)
    public T findOne(I id) {
        return repository.findOne(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public T findStrictOne(I id) {
        T t = findOne(id);
        if (t == null) {
            throw new RuntimeException("未找到指定id的对象");
        }
        return t;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> findAll() {
        return repository.findAll();
    }

    public T add(T t) {
        GenericGenerator generator = t.getClass().getAnnotation(GenericGenerator.class);
        if (!IDG.ASSIGNED.equals(generator.strategy())) {
            t.setId(null);
        }
        beforeAdd(t);
        return repository.save(t);
    }

    protected void beforeAdd(T t) {

    }

    /**
     * 尽量保护update方法，使得不被滥用，如果仅是修改部分字段时，
     * 应该在Service中定义相应的需求方法，底层调用该方法
     */
    protected T update(T t) {
        if (t.getId() == null) {
            throw new RuntimeException("主键id不能为空");
        }
        if (!exists(t.getId())) {
            throw new RuntimeException("数据不存在");
        }
        return repository.save(t);
    }

    public T update(I id, Map<String, Object> map) {
        T oldDomain = findOne(id);
        if (oldDomain == null) {
            throw new RuntimeException("找不到相关对象");
        }
        T newDomain = createDomainFromOldAndMap(oldDomain, map);
        beforeUpdate(oldDomain, newDomain);
        return update(newDomain);
    }

    private T createDomainFromOldAndMap(T oldDomain, Map<String, Object> map) {
        int size = map.entrySet().size();
        String[] ignoreProperties = new String[size];
        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ignoreProperties[i] = CONVERTER.convert(entry.getKey());
            i++;
        }
        T newDomain = (T) objectMapper.convertValue(map, oldDomain.getClass());
        BeanUtils.copyProperties(oldDomain, newDomain, ignoreProperties);
        return newDomain;
    }

    protected void beforeUpdate(T oldDomain, T newDomain) {
    }

    public void delete(I id) {
        repository.delete(id);
    }

    protected void delete(T t) {
        beforeDelete(t);
        repository.delete(t);
    }

    protected void beforeDelete(T t) {
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean exists(I id) {
        return repository.exists(id);
    }
}
