package com.closer.common.helper;

import com.closer.common.config.RdmsConfig;
import com.closer.common.handler.TableProvider;
import com.closer.company.event.CompanyCreateEvent;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * 表处理帮助类
 * Created by closer on 2016/1/5.
 */
@Component
public class TableHelper {

    @Autowired
    private DataSource dataSource;

    public static final ThreadLocal<List<Class>> ENTITY_CLASS = new ThreadLocal<List<Class>>(){
        @Override
        protected List<Class> initialValue() {
            return new LinkedList<>();
        }
    };

    public static void addEntityClass(Class entityClass) {
        List<Class> entityClasses = ENTITY_CLASS.get();
        entityClasses.add(entityClass);
    }

    /**
     * 处理公司新增事件，该处理将于公司新增的保存提交后执行
     * @param event 事件
     */
    @TransactionalEventListener
    public void handleCompanyCreate(CompanyCreateEvent event) {
        try{
            List<Class> entityClasses = ENTITY_CLASS.get();
            createTable(entityClasses, TableProvider.PREFIX, event.getCompany().getShortName());
        }finally {
            ENTITY_CLASS.remove();
        }
    }

    public void createTable(List<Class> entityClasses, String... keyValues) {
        if (keyValues.length % 2 == 1) {
            throw new RuntimeException("创建表时需要替换的字符与值应成对出现");
        }

        String[] sqlArr = entity2Sql(RdmsConfig.DIALECT, entityClasses);

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (Statement stmt = connection.createStatement()) {
            for (String sql : sqlArr) {
                for (int i = 0; i < keyValues.length; i = i + 2) {
                    String key = keyValues[i];
                    String value = keyValues[i + 1];
                    sql = sql.replace(key, value);
                }
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            String msg = "创建表失败";
            throw new RuntimeException(msg, e);
        }finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    private String[] entity2Sql(Dialect dialect, List<Class> entityClasses) {
        Configuration cfg = new Configuration();
        cfg.setNamingStrategy(new ImprovedNamingStrategy());
        for (Class entityClass : entityClasses) {
            cfg.addAnnotatedClass(entityClass);
        }
        return cfg.generateSchemaCreationScript(dialect);
    }

}
