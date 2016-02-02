package com.closer.common.handler;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * 支持分布式
 * Created by closer on 2016/2/2.
 */
public class DistributedIdentifierGenerator implements IdentifierGenerator{
    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return null;
    }
}
