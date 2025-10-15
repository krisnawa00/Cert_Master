package lv.venta.controller;

import lv.venta.model.sertifikati;
import lv.venta.service.impl.SertifikatiCRUDService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("crud")
public class SertifikatiCRUDController {

    @Autowired
    private SertifikatiCRUDService sertCrud;

    @GetMapping("/sertifikati/show/all")//localhost:8080/crud/sertifikati/show/all
    public String getAllSertifikati(Model model) {
        try {
        	ArrayList<sertifikati> sertifikati = sertCrud.retrieveAllSertifikati();
            model.addAttribute("sertifikati", sertifikati);
            return "sertifikatu-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
    
    @GetMapping("/sertifikati/show/{id}")//localhost:8080/crud/sertifikati/show/2
    public String getSertifikatsById(@PathVariable int id, Model model) {
    	try {
            sertifikati s = sertCrud.retrieveSertifikatiById(id);
            model.addAttribute("sertifikats", s);
            return "sertifikats-one-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
    
    
} // viss strada dievs palidz