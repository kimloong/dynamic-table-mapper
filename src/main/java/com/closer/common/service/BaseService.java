package com.closer.common.service;

import com.closer.common.domain.BaseDomain;
import com.closer.common.repository.BaseRepository;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * BaseService
 * Created by closer on 2016/1/5.
 */
@Transactional
public class BaseService<T extends BaseDomain> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public Logger getLog() {
        return log;
    }

    @Autowired
    private BaseRepository<T> repository;

    public static final Converter<String, String> CONVERTER
            = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

    public T findOne(Long id) {
        return repository.findOne(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T add(T t) {
        t.setId(null);
        return repository.save(t);
    }

    public T update(T t) {
        if (t.getId() == null) {
            throw new RuntimeException("主键id不能为空");
        }
        if (!exists(t.getId())) {
            throw new RuntimeException("数据不存在");
        }
        return repository.save(t);
    }

    public T update(Long id, Map<String, Object> map) {
        T t = findOne(id);
        if (t == null) {
            throw new RuntimeException("找不到相关对象");
        }
        BeanMap beanMap = new BeanMap(t);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = CONVERTER.convert(entry.getKey());
            Method method = beanMap.getWriteMethod(key);
            if (method != null) {
                try {
                    method.invoke(t, entry.getValue());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.debug("未找到属性{}", entry.getKey());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(entry.getKey() + "参数类型错误");
                }
            }
        }
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
