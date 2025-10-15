package lv.venta.controller;

import lv.venta.model.Sertifikati;
import lv.venta.service.impl.SertifikatiFilterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("filter")
public class SertifikatiFilterController {

    @Autowired
    private SertifikatiFilterService filterService;

    // Show filter form
    @GetMapping("/sertifikati/form")
    public String showFilterForm(Model model) {
        return "sertifikatu-filter-page";
    }

    // Filter by signature status
    @GetMapping("/sertifikati/parakstits/{status}")
    public String filterByParakstitStatus(@PathVariable boolean status, Model model) {
        try {
            ArrayList<Sertifikati> sertifikati = filterService.filterByParakstitStatus(status);
            model.addAttribute("sertifikati", sertifikati);
            model.addAttribute("filterType", status ? "Parakstīti" : "Neparakstīti");
            return "sertifikatu-filtered-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    // Filter by date range
    @GetMapping("/sertifikati/datums")
    public String filterByDateRange(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {
        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
            
            ArrayList<Sertifikati> sertifikati = filterService.filterByDateRange(start, end);
            model.addAttribute("sertifikati", sertifikati);
            model.addAttribute("filterType", "Datumu diapazons: " + startDate + " līdz " + endDate);
            return "sertifikatu-filtered-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    // Filter by participant
    @GetMapping("/sertifikati/dalibnieks/{kdId}")
    public String filterByParticipant(@PathVariable long kdId, Model model) {
        try {
            ArrayList<Sertifikati> sertifikati = filterService.filterByParticipant(kdId);
            model.addAttribute("sertifikati", sertifikati);
            model.addAttribute("filterType", "Dalībnieka ID: " + kdId);
            return "sertifikatu-filtered-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    // Filter by course
    @GetMapping("/sertifikati/kurss/{kId}")
    public String filterByCourse(@PathVariable long kId, Model model) {
        try {
            ArrayList<Sertifikati> sertifikati = filterService.filterByCourse(kId);
            model.addAttribute("sertifikati", sertifikati);
            model.addAttribute("filterType", "Kursa ID: " + kId);
            return "sertifikatu-filtered-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    // Combined filter
    @PostMapping("/sertifikati/search")
    public String combinedFilter(
            @RequestParam(required = false) Boolean parakstits,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long kdId,
            @RequestParam(required = false) Long kId,
            Model model) {
        try {
            LocalDate start = startDate != null && !startDate.isEmpty() ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null && !endDate.isEmpty() ? LocalDate.parse(endDate) : null;
            
            ArrayList<Sertifikati> sertifikati = filterService.combinedFilter(parakstits, start, end, kdId, kId);
            model.addAttribute("sertifikati", sertifikati);
            model.addAttribute("filterType", "Kombinētā meklēšana");
            return "sertifikatu-filtered-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
}