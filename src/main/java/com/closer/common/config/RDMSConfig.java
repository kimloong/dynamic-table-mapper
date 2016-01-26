package com.closer.common.config;

import com.closer.common.handler.TableMapperInterceptor;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;

/**
 * 关系型数据库配置
 * Created by closer on 2016/1/3.
 */
@Configuration
@EnableJpaRepositories(value = "com.closer",
        includeFilters = {@ComponentScan.Filter(Repository.class)})
@EnableTransactionManagement
@EnableJpaAuditing
public class RDMSConfig {

    public static final Dialect DIALECT = new HSQLDialect();
    private static final String INTERCEPTOR = TableMapperInterceptor.class.getCanonicalName();

    @Bean
    public DataSource dataSource() {
//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        return builder.setType(EmbeddedDatabaseType.HSQL).build();
        //使用单独的HSQL服务
//        java -cp hsqldb-2.3.3.jar org.hsqldb.Server -database.0 testdb -dbname.0 testdbname
//        java -cp hsqldb-2.3.3.jar org.hsqldb.util.DatabaseManager -url jdbc:hsqldb:hsql://localhost/testdbname
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUrl("jdbc:hsqldb:hsql://localhost/testdbname");
        return dataSource;
    }


    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.closer..*.domain");
        factory.setDataSource(dataSource());
        factory.getJpaPropertyMap().put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        factory.getJpaPropertyMap().put("hibernate.ejb.interceptor", INTERCEPTOR);
        factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

}
