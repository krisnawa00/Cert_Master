package lv.venta.controller;

import lv.venta.model.sertifikati;
import lv.venta.service.impl.SertifikatiCRUDService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/CRUD")
public class SertifikatiCRUDController {

    @Autowired
    private SertifikatiCRUDService sertCrud;

    @GetMapping("/sertifikati/show/all")
    public String getAllSertifikati(Model model) {
        try {
            ArrayList<sertifikati> all = sertCrud.retrieveAllSertifikati();
            model.addAttribute("package", all);
            return "sertifikatu-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
    
    @GetMapping("/sertifikati/show/{id}")
    public String getSertifikatsById(@PathVariable long sertId, Model model) {
    	try {
            sertifikati s = sertCrud.retrieveSertifikatsById(sertId);
            model.addAttribute("package", s);
            return "sertifikats-one-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
    
    
}