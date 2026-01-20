package lv.venta.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lv.venta.model.MacibuRezultati;
import lv.venta.service.TranslatorService;
import lv.venta.service.impl.MacibuRezultatiCrudService;

@Controller
@RequestMapping("/crud/maciburezultati")
public class MacibuRezultatiCRUDController {

    @Autowired
    private MacibuRezultatiCrudService macibuRezultatiCRUDService;

    @Autowired
    private TranslatorService translatorService;
    
    @GetMapping("/show/all")
    public String getAllMacibuRezultati(
            @RequestParam(value = "lang", defaultValue = "lv") String lang,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "mrId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            Model model) {
        try {
            // Izveidojam Sort objektu
            Sort sort = direction.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            
            // Izveidojam Pageable objektu
            Pageable pageable = PageRequest.of(page, size, sort);
            
            // Iegūstam lapoto rezultātu
            Page<MacibuRezultati> rezultatiPage = macibuRezultatiCRUDService.retrieveAllMacibuRezultatiPaginated(pageable);
            
            // Tulkojam, ja vajadzīgs
            List<MacibuRezultati> rezultatiList = rezultatiPage.getContent();
            if (!"lv".equals(lang)) {
                for (MacibuRezultati mr : rezultatiList) {
                    if (mr.getKurss() != null) {
                        String translated = translatorService.translateText(mr.getKurss().getNosaukums(), lang);
                        mr.getKurss().setNosaukums(translated);
                    }
                }
            }

            // Pievienojam pagination info
            model.addAttribute("rezultati", rezultatiList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", rezultatiPage.getTotalPages());
            model.addAttribute("totalItems", rezultatiPage.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDirection", direction);

            // Tulkojam galvenes
            model.addAttribute("header_rezultata_id", translatorService.translateText("Rezultāta ID", lang));
            model.addAttribute("header_kursa_nosaukums", translatorService.translateText("Kursa nosaukums", lang));
            model.addAttribute("header_stundas", translatorService.translateText("Stundas", lang));
            model.addAttribute("header_limenis", translatorService.translateText("Līmenis", lang));
            model.addAttribute("header_izieta", translatorService.translateText("Izieta", lang));

            model.addAttribute("currentLanguage", lang);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            return "macibu-rezultati-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/{id}")
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

    @GetMapping("/update/{id}")
    public String getUpdateMacibuRezultati(@PathVariable(name = "id") int id, 
            @RequestParam(value = "lang", defaultValue = "lv") String lang,
            Model model) {
        try {
            MacibuRezultati rezultatiupd = macibuRezultatiCRUDService.retrieveMacibuRezultatiById(id);
            
            if (!"lv".equals(lang) && rezultatiupd.getKurss() != null) {
                String translatedNosaukums = translatorService.translateText(rezultatiupd.getKurss().getNosaukums(), lang);
                rezultatiupd.getKurss().setNosaukums(translatedNosaukums);
            }
            
            model.addAttribute("title", translatorService.translateText("Atjaunināt Mācību Rezultātu", lang));
            model.addAttribute("label_rezultata_id", translatorService.translateText("Rezultāta ID", lang));
            model.addAttribute("label_kursa_id", translatorService.translateText("Kursa ID", lang));
            model.addAttribute("label_kursa_nosaukums", translatorService.translateText("Kursa Nosaukums", lang));
            model.addAttribute("label_stundas", translatorService.translateText("Stundas", lang));
            model.addAttribute("label_limenis", translatorService.translateText("Līmenis", lang));
            model.addAttribute("label_macibu_rezultats", translatorService.translateText("Mācību Rezultāts (Iziets)", lang));
            model.addAttribute("text_iziets_info", translatorService.translateText("Atzīmēt, ja kurss ir sekmīgi pabeigts", lang));
            model.addAttribute("button_save", translatorService.translateText("Saglabāt izmaiņas", lang));
            model.addAttribute("button_cancel", translatorService.translateText("Atcelt", lang));
            model.addAttribute("error_kursa", translatorService.translateText("Kursa kļūda", lang));
            
            model.addAttribute("rezultats", rezultatiupd);
            model.addAttribute("currentLanguage", lang);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            return "update-macibu-rezultati-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postUpdateMacibuRezultati(@Valid MacibuRezultati macibuRezultati, BindingResult result,
            Model model, @PathVariable(name = "id") int id) {
        if (result.hasErrors()) {
            return "update-macibu-rezultati-page";
        }
        try {
            macibuRezultatiCRUDService.updateById(id, macibuRezultati.getKurss(),
                    macibuRezultati.isMacibuRezultats());
            return "redirect:/crud/maciburezultati/show/all";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteMacibuRezultatiById(@PathVariable("id") int id, Model model) {
        try {
            macibuRezultatiCRUDService.deleteMacibuRezultatiById(id);
            return "redirect:/crud/maciburezultati/show/all";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/insert")
    public String getInsertMacibuRezultati(@RequestParam(value = "lang", defaultValue = "lv") String lang,
            Model model) {
        
        model.addAttribute("title", translatorService.translateText("Pievienot Jaunu Rezultātu", lang));
        model.addAttribute("label_kursa_id", translatorService.translateText("Kursa ID", lang));
        model.addAttribute("label_kurss_iziets", translatorService.translateText("Kurss Iziets", lang));
        model.addAttribute("button_add", translatorService.translateText("Pievienot", lang));
        model.addAttribute("link_back", translatorService.translateText("Atpakaļ uz sarakstu", lang));
        
        model.addAttribute("currentLanguage", lang);
        model.addAttribute("languages", translatorService.getAvailableLanguages());

        return "macibu-rezultats-insert-page";
    }

    @PostMapping("/insert")
    public String postInsertMacibuRezultati(@RequestParam("kId") long kId,
            @RequestParam("macibuRezultats") boolean macibuRezultats,
            Model model) {
        try {
            macibuRezultatiCRUDService.insertNewMacibuRezultats(kId, macibuRezultats);
            return "redirect:/crud/maciburezultati/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
}