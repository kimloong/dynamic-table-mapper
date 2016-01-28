package com.closer.common.handler;

import com.closer.common.config.RDMSConfig;
import com.closer.tenant.domain.Tenant;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

/**
 * Created by closer on 2016/1/28.
 */
@Component
public class TableCreateHandler {

    private Logger log = LoggerFactory.getLogger(TableHandler.class);

    @Autowired
    private DataSource dataSource;

    @Async
    public void createTable(Set<Class> entityClasses, Tenant tenant, String... keyValues) {
        System.out.println("----------");
//        TableProvider.setTenant(tenant);
//        _createTable(entityClasses, keyValues);
//        TableProvider.clear();
    }


    private void _createTable(Set<Class> entityClasses, String... keyValues) {
        if (keyValues.length % 2 == 1) {
            throw new RuntimeException("创建表时需要替换的字符与值应成对出现");
        }

        String[] sqlArr = entity2Sql(RDMSConfig.DIALECT, entityClasses);

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (Statement stmt = connection.createStatement()) {
            for (String sql : sqlArr) {
                for (int i = 0; i < keyValues.length; i = i + 2) {
                    String key = keyValues[i];
                    String value = keyValues[i + 1];
                    sql = sql.replace(key, value);
                }
                log.info("建表sql:{}", sql);
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            String msg = "创建表失败";
            throw new RuntimeException(msg, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    private String[] entity2Sql(Dialect dialect, Set<Class> entityClasses) {
        Configuration cfg = new Configuration();
        cfg.setNamingStrategy(new ImprovedNamingStrategy());
        for (Class entityClass : entityClasses) {
            cfg.addAnnotatedClass(entityClass);
        }
        return cfg.generateSchemaCreationScript(dialect);
    }
}
