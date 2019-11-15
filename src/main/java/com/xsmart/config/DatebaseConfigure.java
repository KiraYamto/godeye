package com.xsmart.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 12 14:07
 */
@Configuration
public class DatebaseConfigure {
    @Autowired
    private Environment env;
    @Bean
    public DataSource getDataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setInitialSize(Integer.parseInt(env.getProperty("spring.datasource.initialSize")));
        dataSource.setMaxActive(Integer.parseInt(env.getProperty("spring.datasource.maxActive")));
        dataSource.setMinIdle(Integer.parseInt(env.getProperty("spring.datasource.minIdle")));
        dataSource.setMaxIdle(Integer.parseInt(env.getProperty("spring.datasource.maxIdle")));
        dataSource.setMaxWait(Integer.parseInt(env.getProperty("spring.datasource.maxWait")));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("spring.datasource.testOnBorrow")));
        dataSource.setTestOnReturn(Boolean.parseBoolean(env.getProperty("spring.datasource.testOnReturn")));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(env.getProperty("spring.datasource.testWhileIdle")));
        dataSource.setValidationQuery(env.getProperty("spring.datasource.validationQuery"));
        dataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(env.getProperty("spring.datasource.timeBetweenEvictionRunsMillis")));
        dataSource.setNumTestsPerEvictionRun(Integer.parseInt(env.getProperty("spring.datasource.numTestsPerEvictionRun")));
        dataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(env.getProperty("spring.datasource.minEvictableIdleTimeMillis")));
        dataSource.setRemoveAbandoned(Boolean.parseBoolean(env.getProperty("spring.datasource.removeAbandoned")));
        dataSource.setRemoveAbandonedTimeout(Integer.parseInt(env.getProperty("spring.datasource.removeAbandonedTimeout")));
        return dataSource;
    }

}
