package com.closer.common.handler;

import com.closer.common.config.RDMSConfig;
import com.closer.tenant.domain.Tenant;
import com.closer.tenant.event.TenantCreateEvent;
import com.closer.tenant.service.TenantSupport;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 表处理帮助类
 * Created by closer on 2016/1/5.
 */
@Component
public class TableHandler {


    private static final Logger LOG = LoggerFactory.getLogger(TableHandler.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private List<TenantSupport> tenantSupports;

    @Autowired
    private ApplicationContext applicationContext;

    private Set<Class> entityClasses;

    @PostConstruct
    public void postConstruct() {
        entityClasses = new HashSet<>();
        if (tenantSupports == null) {
            tenantSupports = Collections.emptyList();
            return;
        }
        for (TenantSupport tenantSupport : tenantSupports) {
            entityClasses.addAll(Arrays.asList(tenantSupport.getEntities()));
        }
    }

    /**
     * 处理公司新增事件，该处理将于公司新增的保存提交后执行
     *
     * @param event 事件
     */

    @TransactionalEventListener
    public void handleCompanyCreate(TenantCreateEvent event) {
        TableHandler self = applicationContext.getBean(TableHandler.class);
        self.createTable(entityClasses, event.getTenant());
    }


    @Async
    public void createTable(Set<Class> entityClasses, Tenant tenant) {
        System.out.println(Thread.currentThread().getName());
        TableProvider.setTenant(tenant);
        _createTable(entityClasses, TableProvider.PREFIX, TableProvider.getTablePrefix());
        TableProvider.clear();
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
                sql = make(sql);
                LOG.info("建表sql:{}", sql);
                executeSql(stmt, sql);
            }
        } catch (SQLException e) {
            String msg = "创建表失败";
            throw new RuntimeException(msg, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    private static void executeSql(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            if (!sql.startsWith("alter table")) {
                String msg = "创建表失败";
                throw new RuntimeException(msg, e);
            }
            LOG.warn(e.getMessage());
        }
    }

    /**
     * 对sql进行特殊处理
     */
    private static String make(String sql) {
        //增加if not exists
        if (sql.startsWith("create table")) {
            return sql.replace("create table", "create table if not exists");
        }
        //重命名约束名
        if (sql.contains("add constraint")) {
            String[] fields = sql.split("\\s");
            fields[5] = fields[5] + TableProvider.getTableNum();
            return StringUtils.join(fields, " ");
        }
        return sql;
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
