package com.alineumsoft.zenwk.security.user.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;


@Configuration(proxyBeanMethods = false)
public class DataSourceConfigurations {
	@Bean
	@Primary
	@ConfigurationProperties("app.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties("app.datasource.hikari")
	public HikariDataSource dataSource() {
//	public HikariDataSource dataSource(Environment environment) {
//		 Obtener y vincular las propiedades configuradas
//	    Binder binder = Binder.get(environment);
//	    BindResult<HikariDataSource> bindResult = binder.bind("app.datasource.hikari", Bindable.of(HikariDataSource.class));
//
//	    // Si las propiedades existen, imprimirlas
//	    bindResult.ifBound(dataSource -> {
//	        System.out.println("Hikari DataSource Properties (Before Build):");
//	        System.out.println("JDBC URL: " + dataSource.getJdbcUrl());
//	        System.out.println("Username: " + dataSource.getUsername());
//	        System.out.println("Maximum Pool Size: " + dataSource.getMaximumPoolSize());
//	        System.out.println("Idle Timeout: " + dataSource.getIdleTimeout());
//	    });

		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}


}
