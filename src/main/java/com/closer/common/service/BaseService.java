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

    @Autowired
    private BaseRepository<T> repository;

    public static final Converter<String, String> CONVERTER
            = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

    public T findOne(Long id) {
        return repository.findOne(id);
    }

    public T findStrictOne(Long id) {
        T t = findOne(id);
        if (t == null) {
            throw new RuntimeException("未找到指定id的对象");
        }
        return t;
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
        updateByMap(t, map);
        return update(t);
    }

    private void updateByMap(Object t, Map<String, Object> map) {
        BeanMap beanMap = new BeanMap(t);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = CONVERTER.convert(entry.getKey());
            Object value = entry.getValue();
            Method method = beanMap.getWriteMethod(key);
            if (method != null) {
                Class fieldType = method.getParameterTypes()[0];
                if (BaseDomain.class.isAssignableFrom(fieldType)) {
                    //field的真实类型实例
                    try {
                        Object fieldDomain = fieldType.newInstance();
                        updateByMap(fieldDomain, (Map) value);
                        setFieldValue(method, t, key, fieldDomain);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException("无法实例类型" + fieldType.getCanonicalName()
                                + ",可能由于缺少无参构造函数");
                    }
                } else if (fieldType.equals(Long.class) && value instanceof Integer) {
                    setFieldValue(method, t, key, Long.parseLong(String.valueOf(value)));
                } else {
                    setFieldValue(method, t, key, value);
                }
            }
        }
    }

    private void setFieldValue(Method method, Object t, String key, Object value) {
        try {
            method.invoke(t, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.debug("未找到属性{}", key);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(key + "参数类型错误");
        }
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
