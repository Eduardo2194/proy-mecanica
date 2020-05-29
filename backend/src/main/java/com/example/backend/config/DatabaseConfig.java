package com.example.backend.config;

import com.zaxxer.hikari.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(dbUrl);
		config.setUsername("koqfequxwpulzf");
		config.setPassword("2061bf5db90b2cfc78fa5f79980ddbd9c83dab68977b4b913ae401fc5b77a365");
		config.setMaximumPoolSize(10);
		return new HikariDataSource(config);
	}
		
}