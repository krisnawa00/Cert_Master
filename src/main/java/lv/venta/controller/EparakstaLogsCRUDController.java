package lv.venta.controller;

import java.time.LocalDate;
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
import lv.venta.model.EParakstaLogs;
import lv.venta.service.TranslatorService;
import lv.venta.service.impl.EparakstaLogsCRUDService;

@Slf4j
@Controller
@RequestMapping("/crud/eparakstalogs")
public class EparakstaLogsCRUDController {

    @Autowired
    private EparakstaLogsCRUDService eparakstaLogsCRUDService;

    @Autowired
    private TranslatorService translatorService;
    
    @GetMapping("/show/all")
    public String getAllEparakstaLogs(
            @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "eId") String sortBy,
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
            Page<EParakstaLogs> eparakstsPage = eparakstaLogsCRUDService.retrieveAllEParakstaLogsPaginated(pageable);
            
            // Tulkojam, ja vajadzīgs
            List<EParakstaLogs> eparakstsList = eparakstsPage.getContent();
            if (!"lv".equals(lang)) {
                eparakstsList = translateEparakstaLogsList(new ArrayList<>(eparakstsList), lang);
            }

            // Pievienojam pagination info
            model.addAttribute("eparaksts", eparakstsList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", eparakstsPage.getTotalPages());
            model.addAttribute("totalItems", eparakstsPage.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDirection", direction);

            // Tulkojam galvenes
            model.addAttribute("header_id", translatorService.translateText("ID", lang));
            model.addAttribute("header_sertifikata_id", translatorService.translateText("Sertifikāta ID", lang));
            model.addAttribute("header_parakstisanas_datums", translatorService.translateText("Parakstīšanas datums", lang));
            model.addAttribute("header_statuss", translatorService.translateText("Statuss", lang));

            model.addAttribute("currentLanguage", lang);
            model.addAttribute("languages", translatorService.getAvailableLanguages());
            
            log.info("Veiksmīgi ielādēta e-paraksta logu lapa");
            
            return "eparaksta-logs-page";
        } catch (Exception e) {
        	log.error("Kļūda iegūstot visus e-paraksta logus: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/{id}")
    public String getEparakstaLogById(@PathVariable("id") int id, @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang, Model model) {
    	log.info("GET pieprasījums e-paraksta logam ar ID: {}, valoda: {}", id, lang);
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
            
            log.info("Veiksmīgi ielādēts e-paraksta logs ID: {}", id);

            return "one-eparaksta-logs-page";
        } catch (Exception e) {
        	log.error("Kļūda iegūstot e-paraksta logu ar ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateEparakstaLogsById(@PathVariable(name = "id") int id, @RequestParam(name = "lang", required = false, defaultValue = "lv") String lang, Model model) {
    	log.info("GET pieprasījums e-paraksta loga atjaunināšanai ID: {}", id);
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
            
            return "update-eparaksta-logs-page";
        } catch (Exception e) {
        	log.error("Kļūda ielādējot atjaunināšanas formu e-paraksta logam ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postUpdateEparakstaLogs(@Valid EParakstaLogs eparakstaLogs, BindingResult result,
            Model model, @PathVariable(name = "id") int id) {
    	log.info("POST pieprasījums e-paraksta loga atjaunināšanai ID: {}", id);
        if (result.hasErrors()) {
        	log.warn("Validācijas kļūdas atjauninot e-paraksta logu ID {}: {}", id, result.getAllErrors());
            return "update-eparaksta-logs-page";
        }
        try {
            eparakstaLogsCRUDService.updateById(id, eparakstaLogs.getSertifikati(),
                    eparakstaLogs.getParakstisanasDatums(), eparakstaLogs.getStatuss());
            log.info("Veiksmīgi atjaunināts e-paraksta logs ID: {}", id);
            return "redirect:/crud/eparakstalogs/show/all";
        } catch (Exception e) {
        	log.error("Kļūda atjauninot e-paraksta logu ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteEparakstaLogs(@PathVariable("id") int id, Model model) {
    	log.info("DELETE pieprasījums e-paraksta logam ID: {}", id);
    	try {
            eparakstaLogsCRUDService.deleteMacibuRezultatiById(id);
            log.info("Veiksmīgi izdzēsts e-paraksta logs ID: {}", id);
            return "redirect:/crud/eparakstalogs/show/all";
        } catch (Exception e) {
        	log.error("Kļūda dzēšot e-paraksta logu ID {}: {}", id, e.getMessage(), e);
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
            log.info("Veiksmīgi pievienots jauns e-paraksta logs");
            return "redirect:/crud/eparakstalogs/show/all";
        } catch (Exception e) {
        	log.error("Kļūda pievienojot jaunu e-paraksta logu: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    // Helper metode tulkošanai
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