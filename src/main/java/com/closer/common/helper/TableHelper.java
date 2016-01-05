package com.closer.common.helper;

import com.closer.common.config.RdmsConfig;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 表处理帮助类
 * Created by closer on 2016/1/5.
 */
@Component
public class TableHelper {

    @Autowired
    private DataSource dataSource;

    public void createTable(Class entityClass, String... keyValues) {
        if (keyValues.length % 2 == 1) {
            throw new RuntimeException("创建表时需要替换的字符与值应成对出现");
        }

        String sql = entity2Sql(RdmsConfig.DIALECT, entityClass);

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (Statement stmt = connection.createStatement()) {
            for (int i = 0; i < keyValues.length; i = i + 2) {
                String key = keyValues[i];
                String value = keyValues[i + 1];
                sql = sql.replace(key, value);
            }
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
        DataSourceUtils.releaseConnection(connection, dataSource);
    }

    private String entity2Sql(Dialect dialect, Class entityClass) {
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(entityClass);
        return cfg.generateSchemaCreationScript(dialect)[0];
    }

}
