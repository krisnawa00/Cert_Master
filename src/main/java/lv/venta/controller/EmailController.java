package lv.venta.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lv.venta.service.impl.EmailSendingServiceImpl;

@Controller
public class EmailController {
	
	@Autowired
	private EmailSendingServiceImpl emailService = new EmailSendingServiceImpl();
	
	@GetMapping("/send-email") // localhost:8080/send-email
	public String getEmailSend(Model model) {
		emailService.sendSimpleMsg("s24pekskris@venta.lv", "kristers1906@gmail.com", "TDL School sertifikāts", "Sveiki, šo sūta jums Kristers no TDL School. Jūs esat izdarijis visus kursus. Un pielikumā nosūtu Jūsu TDL School sertifikātu.", new File("C:\\Users\\Kristers\\git\\ProgInz_Seminar2_2025\\src\\main\\resources\\IMG_20231111_232619_355.jpg"));
		model.addAttribute("package", "Epasts uz s24pekskris@venta.lv ir nosūtīts");
		return "data-page";
	}

}