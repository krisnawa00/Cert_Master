package lv.venta.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class EmailSendingServiceImpl{
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendSimpleMsg(String toEmail, String fromEmail, String subject, String text, File attachment) {
		log.info("Sākam sūtīt e-pastu uz: {}", toEmail);
		
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
			
			log.info("E-pasts veiksmīgi nosūtīts uz: {}", toEmail);
			
		} catch (Exception e) {
			log.error("Kļūda sūtot e-pastu uz {}: {}", toEmail, e.getMessage(), e);
            log.error("E-pasta detaļas - Tēma: {}, Teksta garums: {} simboli", 
                subject, text != null ? text.length() : 0);
		}
		
		
		
	}
	
	
}