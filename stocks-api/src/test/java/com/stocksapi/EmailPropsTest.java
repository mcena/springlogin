package com.stocksapi;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.stocksapi.email.config.EmailProps;

class EmailPropsTest {
	private final EmailProps emailProps = new EmailProps();
	@Test
	void testLoadProperties() {
		Properties properties = emailProps.loadProperties();
		
		assertEquals("Activate your account",properties.getProperty("email.setSubject"));
		assertEquals("cenamarcow1@gmail.com",properties.getProperty("email.setFrom"));
		
	}

}
