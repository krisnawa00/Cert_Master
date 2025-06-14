package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import lv.venta.model.MacibuRezultati;
import lv.venta.service.impl.MacibuRezultatiCrudService;





@Controller
@RequestMapping("crud/maciburezultati")
public class MacibuRezultatiCRUDController {

    @Autowired
    private MacibuRezultatiCrudService macibuRezultatiCRUDService; 


    @GetMapping("/maciburezultati/show/all")
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
    
    
    @GetMapping("/maciburezultati/show/{id}")
    public String getMacibuRezultatiById(int id, Model model) {
        try {
            MacibuRezultati rezultats = macibuRezultatiCRUDService.retrieveMacibuRezultatiById(id);
            model.addAttribute("rezultats", rezultats);
            return "macibu-rezultati-details-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
    

    @GetMapping("/maciburezultati/delete/{id}")
    public String deleteMacibuRezultatiById(@PathVariable int id, Model model) {
        try {
            macibuRezultatiCRUDService.deleteMacibuRezultatiById(id);
            model.addAttribute("message", "Macību rezultāts ar ID " + id + " ir veiksmīgi dzēsts.");
            return "success-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
    









}
