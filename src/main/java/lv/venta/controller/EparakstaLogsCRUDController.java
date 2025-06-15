package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lv.venta.model.EParakstaLogs;
import lv.venta.service.impl.EparakstaLogsCRUDService;


@Controller
@RequestMapping("crud")
public class EparakstaLogsCRUDController {


    @Autowired
    private EparakstaLogsCRUDService eparakstaLogsCRUDService; 

    @GetMapping("/eparakstalogs/show/all")//localhost:8080/crud/eparakstalogs/show/all
    public String getAllEparakstaLogs(Model model){

        try {
            Iterable<EParakstaLogs> eparaksts = eparakstaLogsCRUDService.retrieveAllEParakstaLogs();
            model.addAttribute("eparaksts", eparaksts);
            return "eparaksta-logs-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }  // sis strada
    
    @GetMapping("/eparakstalogs/{id}")//localhost:8080/crud/eparakstalogs/1
    public String getEparakstaLogById(@PathVariable("id") int id, Model model) {
    try {
        EParakstaLogs eparakstaLogs = eparakstaLogsCRUDService.retrieveEParakstaLogById(id);
        model.addAttribute("eparaksts", eparakstaLogs);
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

// viss strada


}
