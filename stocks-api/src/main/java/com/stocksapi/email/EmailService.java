package com.stocksapi.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class EmailService implements EmailSender{
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	private final JavaMailSender javaMailSender;
	
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmail(String to, String email) {
		try {
			// load by bean or props
		
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
			mimeMessageHelper.setText(email, true);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject("Confirm your email");
			mimeMessageHelper.setFrom("cenamarcow1@gmail.com");
			
			javaMailSender.send(mimeMessage);
			
		} catch(MessagingException e) {
			logger.error("Failed to send email!", e);
			throw new IllegalStateException("failed to send email!");
		}
		
	}

}
