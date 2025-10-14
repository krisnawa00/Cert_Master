package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lv.venta.model.MacibuRezultati;
import lv.venta.service.impl.MacibuRezultatiCrudService;







@Controller
@RequestMapping("crud")
public class MacibuRezultatiCRUDController {

    @Autowired
    private MacibuRezultatiCrudService macibuRezultatiCRUDService; 


    @GetMapping("/maciburezultati/show/all")//localhost:8080/crud/maciburezultati/show/all
    public String getAllMacibuRezultati(Model model) {
        try {
            Iterable<MacibuRezultati> rezultati = macibuRezultatiCRUDService.retrieveAllMacibuRezultati();
            model.addAttribute("rezultati", rezultati);
            return "macibu-rezultati-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
    
    
    @GetMapping("/maciburezultati/{id}")//localhost:8080/crud/maciburezultati/2
    public String getMacibuRezultatiById(@PathVariable("id")int id, Model model) {
        try {
            MacibuRezultati rezultats = macibuRezultatiCRUDService.retrieveMacibuRezultatiById(id);
            model.addAttribute("rezultats", rezultats);
            return "macibu-rezultats-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
    

    @GetMapping("/maciburezultati/update/{id}") //  localhost:8080/crud/maciburezultati/update/2
    public String getMethodName(@PathVariable(name="id") int id, Model model) {
        
        try
        {
            MacibuRezultati rezultatiupd = macibuRezultatiCRUDService.retrieveMacibuRezultatiById(id);
            model.addAttribute("rezultats", rezultatiupd);
            return "update-macibu-rezultati-page";

        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
    

    @PostMapping("/maciburezultati/update/{id}")
    public String postMethodName(@Valid MacibuRezultati macibuRezultati, BindingResult result,
            Model model, @PathVariable(name = "id") int id) {
        
        if(result.hasErrors()){
            return "update-macibu-rezultati-page";
        }else
        {
            try
            {
                macibuRezultatiCRUDService.updateById(id, macibuRezultati.getKurss(), 
                macibuRezultati.isMacibuRezultats());
                model.addAttribute("rezultati", macibuRezultatiCRUDService.retrieveAllMacibuRezultati());
                return "redirect:/crud/maciburezultati/show/all";
            }catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "error-page";
            }



        }
        
    }
    






    @GetMapping("/maciburezultati/delete/{id}")//localhost:8080/crud/maciburezultati/delete/1
    public String deleteMacibuRezultatiById(@PathVariable("id") int id, Model model) {
        try {
        macibuRezultatiCRUDService.deleteMacibuRezultatiById(id);
        model.addAttribute("rezultati", macibuRezultatiCRUDService.retrieveAllMacibuRezultati());
        return "redirect:/crud/maciburezultati/show/all";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "error-page";
    }
    }
    

// viss iet







}
