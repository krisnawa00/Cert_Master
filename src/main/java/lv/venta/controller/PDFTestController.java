package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import lv.venta.service.IPDFCreatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


@Controller
public class PDFTestController {

    @Autowired
    private IPDFCreatorService pdfCreatorService;


    @GetMapping("/pdf/{dalibnieksId}/{kurssId}")
    public String getPDFController(Model model,@RequestParam(name = "dalibnieksId") int dalibnieksId, @RequestParam(name = "kurssId") int kurssId) 
    {
        
        try
        {
            pdfCreatorService.createCertificateAsPDF(dalibnieksId, kurssId);
            model.addAttribute("message", "PDF izveidots veiksmīgi");
            return "data-page";
        }
        	catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "show-error-page";


        }
    


    }
}
