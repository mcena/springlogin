package com.stocksapi.email.config;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailProps implements IEmailProps{
	
	@Override
	public Properties loadProperties() {
		Properties properties = null;
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("email.properties");
			properties = new Properties();
			properties.load(inputStream);
		} catch (Exception e) {
			throw new IllegalStateException("Error loading properties ", e.getCause());
		}

		return properties;
	}
}
