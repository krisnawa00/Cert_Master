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

import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.Sertifikati;
import lv.venta.service.TranslatorService;
import lv.venta.service.impl.SertifikatiCRUDService;

@Controller
@RequestMapping("crud")
public class SertifikatiCRUDController {

    @Autowired
    private SertifikatiCRUDService sertCrud;

    @Autowired
    private TranslatorService translatorService;


    @GetMapping("/sertifikati/show/all")//localhost:8080/crud/sertifikati/show/all
    public String getAllSertifikati(@RequestParam(name = "lang", required = false, defaultValue = "lv") String lang,Model model) {
        try {
        	ArrayList<Sertifikati> sertifikati = sertCrud.retrieveAllSertifikati();

            
            
            if (!"lv".equals(lang)) {
                sertifikati = translateSertifikatiList(sertifikati, lang);
            }
           
            
            model.addAttribute("header_id", translatorService.translateText("ID", lang));
            model.addAttribute("header_izsniegts", translatorService.translateText("Izsniegts", lang));
            model.addAttribute("header_parakstits", translatorService.translateText("Parakstīts", lang));
            model.addAttribute("header_dalibnieks", translatorService.translateText("Dalībnieks", lang));
            model.addAttribute("header_kursa_datums", translatorService.translateText("Kursa Datums", lang));
            model.addAttribute("header_skatit", translatorService.translateText("Skatīt", lang));

            model.addAttribute("sertifikati", sertifikati);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            model.addAttribute("currentLanguage", lang);
            return "sertifikatu-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
    
    @GetMapping("/sertifikati/show/{id}")//localhost:8080/crud/sertifikati/show/2
    public String getSertifikatsById(@PathVariable int id, 
    @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang,
    Model model) {
    	try {
            Sertifikati s = sertCrud.retrieveSertifikatiById(id);
            
           if (!"lv".equals(lang)) {
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
            return "sertifikats-one-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
    




     private ArrayList<Sertifikati> translateSertifikatiList(ArrayList<Sertifikati> sertifikati, String targetLang) {
        List<String> textsToTranslate = new ArrayList<>();
        
        // Collect all texts that need translation
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

        // Translate all texts in one batch
        List<String> translated = translatorService.translateBatch(textsToTranslate, targetLang);

        // Assign translations back to objects
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



        
        
        
      





 // Helper method to translate a single Sertifikati
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