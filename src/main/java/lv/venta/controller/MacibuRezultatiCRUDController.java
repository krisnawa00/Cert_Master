package lv.venta.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lv.venta.model.MacibuRezultati;
import lv.venta.service.TranslatorService;
import lv.venta.service.impl.MacibuRezultatiCrudService;




@Controller
@RequestMapping("crud")
public class MacibuRezultatiCRUDController {

    @Autowired
    private MacibuRezultatiCrudService macibuRezultatiCRUDService; 

    @Autowired
    private TranslatorService translatorService;


    @GetMapping("/maciburezultati/show/all")//localhost:8080/crud/maciburezultati/show/all
    public String getAllMacibuRezultati(@RequestParam(value = "lang", defaultValue = "lv") String lang,
            Model model) {
        
        try {
        Iterable<MacibuRezultati> rezultati = macibuRezultatiCRUDService.retrieveAllMacibuRezultati();
        
        // Convert to list
        List<MacibuRezultati> rezultatiList = new ArrayList<>();
        rezultati.forEach(rezultatiList::add);
        
        // Simple translation
        if (!"lv".equals(lang)) {
            for (MacibuRezultati mr : rezultatiList) {
                // Just translate the course name
                if (mr.getKurss() != null) {
                    String translated = translatorService.translateText(mr.getKurss().getNosaukums(), lang);
                    mr.getKurss().setNosaukums(translated);
                }
            }
        }
         
            model.addAttribute("header_rezultata_id", translatorService.translateText("Rezultāta ID", lang));
            model.addAttribute("header_kursa_nosaukums", translatorService.translateText("Kursa nosaukums", lang));
            model.addAttribute("header_stundas", translatorService.translateText("Stundas", lang));
            model.addAttribute("header_limenis", translatorService.translateText("Līmenis", lang));
            model.addAttribute("header_izieta", translatorService.translateText("Izieta", lang));


            model.addAttribute("rezultati", rezultati);
            model.addAttribute("currentLanguage", lang);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            return "macibu-rezultati-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
    
    
    @GetMapping("/maciburezultati/{id}")//localhost:8080/crud/maciburezultati/2
    public String getMacibuRezultatiById(@PathVariable("id")int id,@RequestParam(value = "lang", defaultValue = "lv") String lang, 
    Model model) {
        try {
            MacibuRezultati rezultats = macibuRezultatiCRUDService.retrieveMacibuRezultatiById(id);
            

            model.addAttribute("header_id", translatorService.translateText("ID", lang));
            model.addAttribute("header_kursa_id", translatorService.translateText("Kursa ID", lang));
            model.addAttribute("header_macibu_rezultats", translatorService.translateText("Mācību rezultāts", lang));

            model.addAttribute("rezultats", rezultats);
            model.addAttribute("currentLanguage", lang);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
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
    



    

    



}
