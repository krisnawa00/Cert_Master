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
import lombok.extern.slf4j.Slf4j;
import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.Sertifikati;
import lv.venta.service.TranslatorService;
import lv.venta.service.impl.SertifikatiCRUDService;

@Slf4j
@Controller
@RequestMapping("/crud/sertifikati")
public class SertifikatiCRUDController {

    @Autowired
    private SertifikatiCRUDService sertCrud;

    @Autowired
    private TranslatorService translatorService;
    
    
    @GetMapping("/show/all")
    public String getAllSertifikati(
            @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "sertId") String sortBy,
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
            Page<Sertifikati> sertifikatiPage = sertCrud.retrieveAllSertifikatiPaginated(pageable);
            
            // Tulkojam, ja vajadzīgs
            List<Sertifikati> sertifikatiList = sertifikatiPage.getContent();
            if (!"lv".equals(lang)) {
            	log.debug("Tulkojam sertifikātus uz: {}", lang);
//                sertifikati = translateSertifikatiList(sertifikati, lang);
                sertifikatiList = translateSertifikatiList(new ArrayList<>(sertifikatiList), lang);
            }
            
            // Pievienojam pagination info modelim
            model.addAttribute("sertifikati", sertifikatiList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", sertifikatiPage.getTotalPages());
            model.addAttribute("totalItems", sertifikatiPage.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDirection", direction);
            
            // KRITISKS: Pievienojam languages UN currentLanguage PIRMS translation
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            model.addAttribute("currentLanguage", lang);
            
            // Tulkojam galvenes
            model.addAttribute("header_id", translatorService.translateText("ID", lang));
            model.addAttribute("header_izsniegts", translatorService.translateText("Izsniegts", lang));
            model.addAttribute("header_parakstits", translatorService.translateText("Parakstīts", lang));
            model.addAttribute("header_dalibnieks", translatorService.translateText("Dalībnieks", lang));
            model.addAttribute("header_kursa_datums", translatorService.translateText("Kursa Datums", lang));
            model.addAttribute("header_skatit", translatorService.translateText("Skatīt", lang));

            model.addAttribute("sertifikati", sertifikatiList);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            model.addAttribute("currentLanguage", lang);
            
            log.info("Veiksmīgi ielādēta sertifikātu lapa");
            
            return "sertifikatu-page";
        } catch (Exception e) {
        	log.error("Kļūda iegūstot visus sertifikātus: {}", e.getMessage(), e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/{id}")
    public String getSertifikatsById(@PathVariable int id, 
    @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang,
    Model model) {
    	log.info("GET pieprasījums sertifikātam ar ID: {}, valoda: {}", id, lang);
    	
    	try {
            Sertifikati s = sertCrud.retrieveSertifikatiById(id);
            
            if (!"lv".equals(lang)) {
            	log.debug("Tulkojam sertifikātu ID={} uz: {}", id, lang);
                s = translateSingleSertifikats(s, lang);
            }
            
            model.addAttribute("label_id", translatorService.translateText("ID", lang));
            model.addAttribute("label_izsniegts", translatorService.translateText("Izsniegts", lang));
            model.addAttribute("label_parakstits", translatorService.translateText("Parakstīts", lang));
            model.addAttribute("label_dalibnieks", translatorService.translateText("Dalībnieks", lang));
            model.addAttribute("label_kursa_datums", translatorService.translateText("Kursa Datums", lang));
            
            model.addAttribute("sertifikats", s);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            model.addAttribute("currentLanguage", lang);
            
            log.info("Veiksmīgi ielādēts sertifikāts ID: {}", id);
            
            return "sertifikats-one-page";
        } catch (Exception e) {
        	log.error("Kļūda iegūstot sertifikātu ar ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateSertifikatsById(@PathVariable(name = "id") int id, 
    @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang,
    Model model) {
    	log.info("GET pieprasījums sertifikāta atjaunināšanai ID: {}", id);
    	try {
            Sertifikati sertifikatsForUpdating = sertCrud.retrieveSertifikatiById(id);
            
            model.addAttribute("title", translatorService.translateText("Atjaunināt Sertifikātu", lang));
            model.addAttribute("label_id", translatorService.translateText("ID", lang));
            model.addAttribute("label_dalibnieka_id", translatorService.translateText("Dalībnieka ID", lang));
            model.addAttribute("label_dalibnieks", translatorService.translateText("Dalībnieks", lang));
            model.addAttribute("label_kursa_datuma_id", translatorService.translateText("Kursa Datuma ID", lang));
            model.addAttribute("label_kursa_datums", translatorService.translateText("Kursa Datums", lang));
            model.addAttribute("label_izsniegts_datums", translatorService.translateText("Izsniegts Datums", lang));
            model.addAttribute("label_parakstits", translatorService.translateText("Parakstīts", lang));
            model.addAttribute("button_save", translatorService.translateText("Saglabāt izmaiņas", lang));
            model.addAttribute("button_cancel", translatorService.translateText("Atcelt", lang));
            model.addAttribute("error_dalibnieka", translatorService.translateText("Dalībnieka kļūda", lang));
            model.addAttribute("error_kursa_datuma", translatorService.translateText("Kursa datuma kļūda", lang));
            model.addAttribute("error_datuma", translatorService.translateText("Datuma kļūda", lang));
            
            model.addAttribute("sertifikats", sertifikatsForUpdating);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            model.addAttribute("currentLanguage", lang);
            
            log.info("Ielādēta atjaunināšanas forma sertifikātam ID: {}", id);
            
            return "update-sertifikats";
        } catch (Exception e) {
        	log.error("Kļūda ielādējot atjaunināšanas formu sertifikātam ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postUpdateSertifikatsById(@Valid Sertifikati sertifikats, BindingResult result,
            Model model, @PathVariable(name = "id") int id) {
    	log.info("POST pieprasījums sertifikāta atjaunināšanai ID: {}", id);
        if (result.hasErrors()) {
        	log.warn("Validācijas kļūdas atjauninot sertifikātu ID {}: {}", id, result.getAllErrors());
            return "update-sertifikats";
        }
        try {
            sertCrud.updateById(id, sertifikats.getDalibnieks(), sertifikats.getKursaDatums(),
                    sertifikats.getIzsniegtsDatums(), sertifikats.isParakstits());
            
            log.info("Veiksmīgi atjaunināts sertifikāts ID: {}", id);
            return "redirect:/crud/sertifikati/show/all";
        } catch (Exception e) {
        	log.error("Kļūda atjauninot sertifikātu ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSertifikatsById(@PathVariable("id") int id, Model model) {
    	log.info("DELETE pieprasījums sertifikātam ID: {}", id);
        try {
            sertCrud.deleteSertifikatiById(id);
            log.info("Veiksmīgi izdzēsts sertifikāts ID: {}", id);
            return "redirect:/crud/sertifikati/show/all";
        } catch (Exception e) {
        	log.error("Kļūda dzēšot sertifikātu ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/insert")
    public String getInsertSertifikats(@RequestParam(name = "lang", required = false, defaultValue = "lv") String lang,
    Model model) {
        
    	log.info("GET pieprasījums jauna sertifikāta pievienošanai, valoda: {}", lang);
        
        model.addAttribute("title", translatorService.translateText("Pievienot Jaunu Sertifikātu", lang));
        model.addAttribute("label_kursa_dalibnieka_id", translatorService.translateText("Kursa Dalībnieka ID", lang));
        model.addAttribute("label_kursa_datuma_id", translatorService.translateText("Kursa Datuma ID", lang));
        model.addAttribute("label_izsniegts_datums", translatorService.translateText("Izsniegšanas Datums", lang));
        model.addAttribute("label_parakstits", translatorService.translateText("Parakstīts", lang));
        model.addAttribute("button_add", translatorService.translateText("Pievienot", lang));
        model.addAttribute("link_back", translatorService.translateText("Atpakaļ uz sarakstu", lang));
        
        model.addAttribute("languages", translatorService.getAvailableLanguages());
        model.addAttribute("currentLanguage", lang);
        return "sertifikats-insert-page";
    }

    @PostMapping("/insert")
    public String postInsertSertifikats(@RequestParam("kdId") long kdId,
            @RequestParam("kdatId") long kdatId,
            @RequestParam("izsniegtsDatums") String izsniegtsDatums,
            @RequestParam("parakstits") boolean parakstits,
            Model model) {
    	log.info("POST pieprasījums jauna sertifikāta pievienošanai: kdId={}, kdatId={}, parakstīts={}", 
                kdId, kdatId, parakstits);
    	try {
            sertCrud.insertNewSertifikats(kdId, kdatId, izsniegtsDatums, parakstits);
            return "redirect:/crud/sertifikati/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    // Helper metodes tulkošanai
    private ArrayList<Sertifikati> translateSertifikatiList(ArrayList<Sertifikati> sertifikati, String targetLang) {
        List<String> textsToTranslate = new ArrayList<>();
        
        for (Sertifikati s : sertifikati) {
            KursaDalibnieks d = s.getDalibnieks();
            KursaDatumi kd = s.getKursaDatums();

            if (d != null) {
                textsToTranslate.add(d.getVards()); 
                textsToTranslate.add(d.getUzvards()); 
            }

            if (kd != null && kd.getKurss() != null) {
                textsToTranslate.add(kd.getKurss().getNosaukums()); 
            }
        }

        List<String> translated = translatorService.translateBatch(textsToTranslate, targetLang);

        int i = 0;
        for (Sertifikati s : sertifikati) {
            KursaDalibnieks d = s.getDalibnieks();
            KursaDatumi kd = s.getKursaDatums();

            if (d != null) {
                d.setVards(translated.get(i++));
                d.setUzvards(translated.get(i++));
            }
            if (kd != null && kd.getKurss() != null) {
                kd.getKurss().setNosaukums(translated.get(i++));
            }
        }
        
        return sertifikati;
    }

    private Sertifikati translateSingleSertifikats(Sertifikati s, String targetLang) {
        List<String> textsToTranslate = new ArrayList<>();
        KursaDalibnieks d = s.getDalibnieks();
        KursaDatumi kd = s.getKursaDatums();

        if (d != null) {
            textsToTranslate.add(d.getVards());
            textsToTranslate.add(d.getUzvards());
        }
        if (kd != null && kd.getKurss() != null) {
            textsToTranslate.add(kd.getKurss().getNosaukums());
        }

        List<String> translated = translatorService.translateBatch(textsToTranslate, targetLang);

        int i = 0;
        if (d != null) {
            d.setVards(translated.get(i++));
            d.setUzvards(translated.get(i++));
        }
        if (kd != null && kd.getKurss() != null) {
            kd.getKurss().setNosaukums(translated.get(i++));
        }

        return s;
    }
}