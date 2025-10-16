package lv.venta.controller;

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
import lv.venta.model.MacibuRezultati;
import lv.venta.service.impl.MacibuRezultatiCrudService;







@Controller
@RequestMapping("/crud/maciburezultati")
public class MacibuRezultatiCRUDController {

    @Autowired
    private MacibuRezultatiCrudService macibuRezultatiCRUDService;

    @GetMapping("/show/all")
    public String getAllMacibuRezultati(Model model) {
        try {
            Iterable<MacibuRezultati> rezultati = macibuRezultatiCRUDService.retrieveAllMacibuRezultati();
            model.addAttribute("rezultati", rezultati);
            return "macibu-rezultati-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/{id}")
    public String getMacibuRezultatiById(@PathVariable("id") int id, Model model) {
        try {
            MacibuRezultati rezultats = macibuRezultatiCRUDService.retrieveMacibuRezultatiById(id);
            model.addAttribute("rezultats", rezultats);
            return "macibu-rezultats-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateMacibuRezultati(@PathVariable(name = "id") int id, Model model) {
        try {
            MacibuRezultati rezultatiupd = macibuRezultatiCRUDService.retrieveMacibuRezultatiById(id);
            model.addAttribute("rezultats", rezultatiupd);
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
    public String getInsertMacibuRezultati(Model model) {
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

