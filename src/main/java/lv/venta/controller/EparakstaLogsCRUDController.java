package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
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

    @GetMapping("/eparakstalogs/update/{id}") //  localhost:8080/crud/eparakstalogs/update/1
    public String getUpdateEparakstaLogsById(@PathVariable(name="id") int id, Model model) 
    {
        try
        {
            EParakstaLogs eparakstaLogsUpdate = eparakstaLogsCRUDService.retrieveEParakstaLogById(id);
            model.addAttribute("eparaksts", eparakstaLogsUpdate);
            return "update-eparaksta-logs-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
}

    @PostMapping("/eparakstalogs/update/{id}")
    public String postMethodName(@Valid EParakstaLogs eparakstaLogs, BindingResult result,
            Model model, @PathVariable(name = "id") int id) {
       if (result.hasErrors()) {
            return "update-eparaksta-logs-page";
        } else {

            try {
                eparakstaLogsCRUDService.updateById(id, eparakstaLogs.getSertifikati(), eparakstaLogs.getParakstisanasDatums(), eparakstaLogs.getStatuss());
                model.addAttribute("eparaksts", eparakstaLogsCRUDService.retrieveAllEParakstaLogs());
                return "redirect:/crud/eparakstalogs/show/all";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "error-page";


            }

        }
    }
    







    @GetMapping("/eparakstalogs/delete/{id}")//localhost:8080/crud/eparakstalogs/delete/1
    public String deleteEparakstaLogs(@PathVariable("id") int id,Model model){
        try {
            eparakstaLogsCRUDService.deleteMacibuRezultatiById(id);
            model.addAttribute("eparaksts", eparakstaLogsCRUDService.retrieveAllEParakstaLogs());
            return "redirect:/crud/eparakstalogs/show/all";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }



    }

// viss strada


}
