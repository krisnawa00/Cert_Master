package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    

    @GetMapping("/maciburezultati/delete/{id}")//localhost:8080/crud/maciburezultati/delete/1
    public String deleteMacibuRezultatiById(@PathVariable("id") int id, Model model) {
        try {
        macibuRezultatiCRUDService.deleteMacibuRezultatiById(id);
        model.addAttribute("rezultati", macibuRezultatiCRUDService.retrieveAllMacibuRezultati());
        return "macibu-rezultati-page";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "error-page";
    }
    }
    

// viss iet

    @GetMapping("/maciburezultati/insert")//localhost:8080/crud/maciburezultati/insert
    public String getInsertMacibuRezultati(Model model) {
        return "macibu-rezultats-insert-page";
    }
    
    @PostMapping("/maciburezultati/insert")//localhost:8080/crud/maciburezultati/insert
    public String postInsertMacibuRezultati(@RequestParam("kId") long kId,
                                            @RequestParam("macibuRezultats") boolean macibuRezultats,
                                            Model model) {
        try {
            macibuRezultatiCRUDService.insertNewMacibuRezultats(kId, macibuRezultats);
            model.addAttribute("rezultati", macibuRezultatiCRUDService.retrieveAllMacibuRezultati());
            return "macibu-rezultati-page";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
}

