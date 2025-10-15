package lv.venta.controller;

import lv.venta.model.sertifikati;
import lv.venta.service.impl.SertifikatiCRUDService;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lv.venta.model.Sertifikati;
import lv.venta.service.impl.SertifikatiCRUDService;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

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
    

    @GetMapping("/sertifikati/update/{id}") //  localhost:8080/crud/sertifikati/update/2
    public String getUpdateSertifikatsById(@PathVariable(name="id") int id, Model model) {
        try {
            Sertifikati sertifikatsForUpdating = sertCrud.retrieveSertifikatiById(id);
            model.addAttribute("sertifikats", sertifikatsForUpdating);
            return "update-sertifikats";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
    
    
    @PostMapping("/sertifikati/update/{id}")
     public String postUpdateSertifikatsById(@Valid Sertifikati sertifikats, BindingResult result,
            Model model, @PathVariable(name = "id") int id)
        {
             if(result.hasErrors()) {
            return "update-sertifikats";
        } else {

            try {
                 sertCrud.updateById(id, sertifikats.getDalibnieks(), sertifikats.getKursaDatums(),
                    sertifikats.getIzsniegtsDatums(), sertifikats.isParakstits());
                model.addAttribute("sertifikati", sertCrud.retrieveAllSertifikati());
                return "redirect:/crud/sertifikati/show/all";

            } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }



        }



        }
    
    
    
    
    //added delete by id not tested though

    @GetMapping("/sertifikati/delete/{id}")//localhost:8080/crud/sertifikati/delete/2
    public String deleteSertifikatsById(@PathVariable("id") int id, Model model)
    {

        try
        {
            sertCrud.deleteSertifikatiById(id);
            model.addAttribute("sertifikati", sertCrud.retrieveAllSertifikati());
            return "redirect:/crud/sertifikati/show/all";
        }catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }




    }


    
} // viss strada dievs palidz