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

import lv.venta.model.EParakstaLogs;
import lv.venta.service.TranslatorService;
import lv.venta.service.impl.EparakstaLogsCRUDService;


@Controller
@RequestMapping("crud")
public class EparakstaLogsCRUDController {


    @Autowired
    private EparakstaLogsCRUDService eparakstaLogsCRUDService; 

    @Autowired
    private TranslatorService translatorService;


    @GetMapping("/eparakstalogs/show/all")//localhost:8080/crud/eparakstalogs/show/all
    public String getAllEparakstaLogs( @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang, Model model){

        try {
            Iterable<EParakstaLogs> eparaksts = eparakstaLogsCRUDService.retrieveAllEParakstaLogs();
        
        // Convert to list
            List<EParakstaLogs> eparakstsList = new ArrayList<>();
            eparaksts.forEach(eparakstsList::add);
        
        
        if (!"lv".equals(lang)) {
            eparakstsList = translateEparakstaLogsList(eparakstsList, lang);
        }

        
            model.addAttribute("header_id", translatorService.translateText("ID", lang));
            model.addAttribute("header_sertifikata_id", translatorService.translateText("Sertifikāta ID", lang));
            model.addAttribute("header_parakstisanas_datums", translatorService.translateText("Parakstīšanas datums", lang));
            model.addAttribute("header_statuss", translatorService.translateText("Statuss", lang));


            model.addAttribute("eparaksts", eparakstsList);
            model.addAttribute("currentLanguage", lang);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            return "eparaksta-logs-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }  // sis strada
    
    @GetMapping("/eparakstalogs/{id}")//localhost:8080/crud/eparakstalogs/1
    public String getEparakstaLogById(@PathVariable("id") int id, @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang, Model model) {
    try {
        EParakstaLogs eparakstaLogs = eparakstaLogsCRUDService.retrieveEParakstaLogById(id);
        
        if (!"lv".equals(lang) && eparakstaLogs.getStatuss() != null) {
            String translatedStatus = translatorService.translateText(eparakstaLogs.getStatuss(), lang);
            eparakstaLogs.setStatuss(translatedStatus);
        }
        
            model.addAttribute("header_eparaksta_id", translatorService.translateText("E-paraksta ID", lang));
            model.addAttribute("header_sertifikata_id", translatorService.translateText("Sertifikāta ID", lang));
            model.addAttribute("header_parakstisanas_datums", translatorService.translateText("Parakstīšanas datums", lang));
            model.addAttribute("header_statuss", translatorService.translateText("Statuss", lang));
        
        model.addAttribute("eparaksts", eparakstaLogs);
        model.addAttribute("currentLanguage", lang);
        model.addAttribute("languages", translatorService.getAvailableLanguages());
        return "one-eparaksta-logs-page";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "error-page";
    }
}

    @GetMapping("/eparakstalogs/delete/{id}")//localhost:8080/crud/eparakstalogs/delete/1
    public String deleteEparakstaLogs(@PathVariable("id") int id,Model model){
        try {
            eparakstaLogsCRUDService.deleteMacibuRezultatiById(id);
            model.addAttribute("eparaksts", eparakstaLogsCRUDService.retrieveAllEParakstaLogs());
            return "eparaksta-logs-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }



    }

private List<EParakstaLogs> translateEparakstaLogsList(List<EParakstaLogs> originalList, String targetLang) {
    
    if ("lv".equals(targetLang)) {
        return originalList;
    }
    
    
    for (EParakstaLogs log : originalList) {
        if (log.getStatuss() != null) {
            String translatedStatus = translatorService.translateText(log.getStatuss(), targetLang);
            log.setStatuss(translatedStatus);
        }
    }
    return originalList;
}






}
