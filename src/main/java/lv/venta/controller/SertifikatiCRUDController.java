package lv.venta.controller;

import lv.venta.model.sertifikati;
import lv.venta.service.SertifikatiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/sertifikats")
public class SertifikatiCRUDController {

    @Autowired
    private SertifikatiService sertifikatiService;

    @GetMapping("/show/all") // localhost:8080/sertifikats/show/all
    public String getAllSertifikati(Model model) {
        try {
            ArrayList<sertifikati> allSertifikati = (ArrayList<sertifikati>) sertifikatiService.findAll();
            model.addAttribute("package", allSertifikati);
            return "sertifikati-all-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
}