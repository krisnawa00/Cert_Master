package lv.venta.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lv.venta.model.EParakstaLogs;
import lv.venta.service.TranslatorService;
import lv.venta.service.impl.EparakstaLogsCRUDService;



@Controller
@RequestMapping("/crud/eparakstalogs")
public class EparakstaLogsCRUDController {

    @Autowired
    private EparakstaLogsCRUDService eparakstaLogsCRUDService;

    
    @Autowired
    private TranslatorService translatorService;
    
    
    @GetMapping("/show/all") // 
    public String getAllEparakstaLogs( @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang, Model model) {
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
    }

    @GetMapping("/{id}")
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

    @GetMapping("/update/{id}")
    public String getUpdateEparakstaLogsById(@PathVariable(name = "id") int id, @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang, Model model) {
        try {
            EParakstaLogs eparakstaLogsUpdate = eparakstaLogsCRUDService.retrieveEParakstaLogById(id);
            
            model.addAttribute("title", translatorService.translateText("Atjaunināt E-paraksta Logu", lang));
            model.addAttribute("label_eparaksta_id", translatorService.translateText("E-paraksta ID", lang));
            model.addAttribute("label_sertifikata_id", translatorService.translateText("Sertifikāta ID", lang));
            model.addAttribute("label_sertifikats", translatorService.translateText("Sertifikāts", lang));
            model.addAttribute("label_sertifikats_nr", translatorService.translateText("Sertifikāts Nr.", lang));
            model.addAttribute("label_parakstisanas_datums", translatorService.translateText("Parakstīšanas Datums", lang));
            model.addAttribute("label_statuss", translatorService.translateText("Statuss", lang));
            model.addAttribute("error_sertifikata", translatorService.translateText("Sertifikāta kļūda", lang));
            model.addAttribute("error_datuma", translatorService.translateText("Datuma kļūda", lang));
            model.addAttribute("error_statusa", translatorService.translateText("Statusa kļūda", lang));
            model.addAttribute("button_save", translatorService.translateText("Saglabāt izmaiņas", lang));
            model.addAttribute("button_cancel", translatorService.translateText("Atcelt", lang));
            
            model.addAttribute("eparaksts", eparakstaLogsUpdate);
            model.addAttribute("currentLanguage", lang);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            
            model.addAttribute("eparaksts", eparakstaLogsUpdate);
            return "update-eparaksta-logs-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postUpdateEparakstaLogs(
        @PathVariable(name = "id") int id,
        @RequestParam("parakstisanasDatums") String parakstisanasDatums,
        @RequestParam("statuss") String statuss,
        Model model) {
        
            
        try {
        // Get the existing log entry
        EParakstaLogs existingLog = eparakstaLogsCRUDService.retrieveEParakstaLogById(id);
        
        // Parse the date
        LocalDate datums = LocalDate.parse(parakstisanasDatums);
        
        // Update with existing certificate
        eparakstaLogsCRUDService.updateById(id, existingLog.getSertifikati(), datums, statuss);
            return "redirect:/crud/eparakstalogs/show/all";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteEparakstaLogs(@PathVariable("id") int id, Model model) {
        try {
            eparakstaLogsCRUDService.deleteMacibuRezultatiById(id);
            return "redirect:/crud/eparakstalogs/show/all";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/insert")
    public String getInsertEparakstaLogs(@RequestParam(name = "lang", required = false, defaultValue = "lv") String lang, Model model) {
        
        model.addAttribute("title", translatorService.translateText("Pievienot Jaunu E-Paraksta Logu", lang));
        model.addAttribute("label_sertifikata_id", translatorService.translateText("Sertifikāta ID", lang));
        model.addAttribute("label_parakstisanas_datums", translatorService.translateText("Parakstīšanas Datums", lang));
        model.addAttribute("label_statuss", translatorService.translateText("Statuss", lang));
        model.addAttribute("option_choose", translatorService.translateText("-- Izvēlēties --", lang));
        model.addAttribute("option_parakstits", translatorService.translateText("Parakstīts", lang));
        model.addAttribute("option_neparakstits", translatorService.translateText("Neparakstīts", lang));
        model.addAttribute("option_gaida", translatorService.translateText("Gaida parakstu", lang));
        model.addAttribute("button_add", translatorService.translateText("Pievienot", lang));
        model.addAttribute("link_back", translatorService.translateText("Atpakaļ uz sarakstu", lang));
        
        model.addAttribute("currentLanguage", lang);
        model.addAttribute("languages", translatorService.getAvailableLanguages());
        return "eparaksta-logs-insert-page";
    }

    @PostMapping("/insert")
    public String postInsertEparakstaLogs(@RequestParam("sertId") long sertId,
            @RequestParam("parakstisanasDatums") String parakstisanasDatums,
            @RequestParam("statuss") String statuss,
            Model model) {
        try {
            eparakstaLogsCRUDService.insertNewEParakstaLogs(sertId, parakstisanasDatums, statuss);
            return "redirect:/crud/eparakstalogs/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
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

