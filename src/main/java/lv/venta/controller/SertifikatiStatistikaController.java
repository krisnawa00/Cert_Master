package lv.venta.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lv.venta.service.impl.SertifikatiStatistikaServiceImpl;

@Controller
@RequestMapping("/statistika")
public class SertifikatiStatistikaController {

    @Autowired
    private SertifikatiStatistikaServiceImpl statistikaService;

    @GetMapping("/visparejas")
    public String getGeneralStatistics(Model model) {
        try {
            Map<String, Object> stats = statistikaService.getGeneralStatistics();
            model.addAttribute("statistics", stats);
            return "statistika-visparejas-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/pa-menesiem")
    public String getMonthlyStatistics(Model model) {
        try {
            Map<String, Long> stats = statistikaService.getStatisticsByMonth();
            model.addAttribute("monthlyStats", stats);
            return "statistika-menesi-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/pa-kursiem")
    public String getCourseStatistics(Model model) {
        try {
            Map<String, Long> stats = statistikaService.getStatisticsByCourse();
            model.addAttribute("courseStats", stats);
            return "statistika-kursi-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/pa-pilsetam")
    public String getCityStatistics(Model model) {
        try {
            Map<String, Long> stats = statistikaService.getStatisticsByCity();
            model.addAttribute("cityStats", stats);
            return "statistika-pilsetas-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        try {
            model.addAttribute("generalStats", statistikaService.getGeneralStatistics());
            model.addAttribute("monthlyStats", statistikaService.getStatisticsByMonth());
            model.addAttribute("courseStats", statistikaService.getStatisticsByCourse());
            model.addAttribute("cityStats", statistikaService.getStatisticsByCity());
            return "statistika-dashboard-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
}