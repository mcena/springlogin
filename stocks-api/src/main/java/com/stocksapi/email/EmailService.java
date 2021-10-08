package com.stocksapi.email;

import java.util.Properties;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.stocksapi.email.config.EmailProps;
import com.stocksapi.email.config.IEmailProps;

@Service
@Async
public class EmailService implements EmailSender{
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	// Properties key
	private static final String SUBJECT_PROPERTY = "email.setSubject";
	private static final String SETFROM_PROPERTY = "email.setFrom";
	
	private final JavaMailSender javaMailSender;
	private final IEmailProps emailProps;
	
	public EmailService(JavaMailSender javaMailSender, IEmailProps emailProps) {
		this.javaMailSender = javaMailSender;
		this.emailProps = emailProps;
	}

	@Override
	public void sendEmail(String to, String email) {
		try {
			// load properties
			Properties properties = emailProps.loadProperties();
			String subject = properties.getProperty(SUBJECT_PROPERTY);
			String setFrom = properties.getProperty(SETFROM_PROPERTY);
			
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
			mimeMessageHelper.setText(email, true);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setFrom(setFrom);
			
			javaMailSender.send(mimeMessage);
			
		} catch(MessagingException e) {
			logger.error("Failed to send email!", e);
			throw new IllegalStateException("failed to send email!");
		}
		
	}

}
