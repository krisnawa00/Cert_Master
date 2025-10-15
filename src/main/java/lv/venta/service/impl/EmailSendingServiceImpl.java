package lv.venta.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendingServiceImpl{
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendSimpleMsg(String toEmail, String fromEmail, String subject, String text, File attachment) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromEmail);
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(text);

			FileSystemResource file = new FileSystemResource(attachment);
			helper.addAttachment(file.getFilename(), file);
			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		
	}
	
	
}