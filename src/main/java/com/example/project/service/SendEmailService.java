/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SendEmailService {
	 @Value("${spring.mail.username}")
	private String from;
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail,String subject,String content) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(from);
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(content);
		mailSender.send(message);
	}
	
	public void sendHtmlEmail(String toEmail,String subject,String content) throws MessagingException {

		MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");
		helper.setFrom(from);
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(content,true);
		
		mailSender.send(mimeMessage);
	}
}
