package com.alineumsoft.zenwk.security.user.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;


@Configuration(proxyBeanMethods = false)
public class DataSourceConfiguration {
	@Bean
	@Primary
	@ConfigurationProperties("app.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties("app.datasource.hikari")
	public HikariDataSource dataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}


}
