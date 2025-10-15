 package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lv.venta.service.IPDFCreatorService;


@Controller
public class PDFTestController {

    @Autowired
    private IPDFCreatorService pdfCreatorService;


    @GetMapping("/pdf/{dalibnieksId}/{kurssId}") // localhost:8080/pdf/1/1
    public String getPDFController(Model model,
    @PathVariable(name = "dalibnieksId") int dalibnieksId,
    @PathVariable(name = "kurssId") int kurssId) 
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
