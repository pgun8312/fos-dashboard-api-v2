package com.fos.api.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@Slf4j
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    private String productServiceJdbcUrlKey;

    @Value("${spring.datasource.username}")
    private String productServiceUserKey;

    @Value("${spring.datasource.password}")
    private String productServicePasswordKey;

    @Bean(name="productServiceDataSource")
    public DataSource productServiceDataSource() throws PropertyVetoException {
        // C3p0 is library for managing database connections
        ComboPooledDataSource datasource = new ComboPooledDataSource();
        datasource.setDriverClass("com.mysql.cj.jdbc.Driver");
        datasource.setJdbcUrl(productServiceJdbcUrlKey);
        datasource.setUser(productServiceUserKey);
        datasource.setPassword(productServicePasswordKey);
        datasource.setInitialPoolSize(5);
        datasource.setMinPoolSize(5);
        datasource.setMaxPoolSize(20);


        log.info("Loaded database configuration for product service db: " + System.getenv("PRODUCT_DB_URL"));
        return datasource;

    }

}
