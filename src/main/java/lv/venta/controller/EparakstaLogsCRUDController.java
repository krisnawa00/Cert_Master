package lv.venta.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.sertifikati;
import lv.venta.service.impl.EparakstaLogsCRUDService;


@Controller
@RequestMapping("crud/eparakstalogs")
public class EparakstaLogsCRUDController {


    @Autowired
    private EparakstaLogsCRUDService eparakstaLogsCRUDService; 

    @GetMapping("/eparakstalogs/show/all")
    public String getAllEparakstaLogs(Model model){

        try {
            Iterable<EParakstaLogs> eparaksts = eparakstaLogsCRUDService.retrieveAllEParakstaLogs();
            model.addAttribute("eparaksts", eparaksts);
            return "eparaksta-logs-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }  
    
    @GetMapping("/eparakstalogs/{id}")
    public String getEparakstaLogById(@PathVariable("id") int id, Model model) {
    try {
        EParakstaLogs eparakstaLogs = eparakstaLogsCRUDService.retrieveEParakstaLogById(id);
        model.addAttribute("eparakstaLogs", eparakstaLogs);
        return "one-eparaksta-logs-page";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "error-page";
    }
}

    @GetMapping("/eparakstalogs/delete/{id}")
    public String deleteEparakstaLogs(@PathVariable("id") int id,Model model){
        try {
            eparakstaLogsCRUDService.deleteMacibuRezultatiById(id);
            model.addAttribute("rezultati", eparakstaLogsCRUDService.retrieveAllEParakstaLogs());
            return "eparaksta-logs-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }



    }

    @GetMapping("/eparakstalogs/add") // localhost:8080/crud/eparakstalogs/add
    public String showAddEParakstaLogForm(Model model) {
        model.addAttribute("eparaksts", new EParakstaLogs());
        return "eparaksta-logs-add-page";
    }


    @PostMapping("/eparakstalogs/add")
    public String addEParakstaLog(
            @RequestParam("sertifikataId") sertifikati sertifikats,
            @RequestParam("parakstisanasDatums") String parakstisanasDatums,
            @RequestParam("statuss") String statuss,
            Model model) {
        try {
            LocalDate datums = LocalDate.parse(parakstisanasDatums);
            eparakstaLogsCRUDService.create(sertifikats, datums, statuss);
            return "redirect:/crud/eparakstalogs/show/all";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }


}
