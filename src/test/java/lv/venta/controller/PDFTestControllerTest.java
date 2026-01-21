package lv.venta.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import lv.venta.service.IPDFCreatorService;

class PDFTestControllerTest {

    @Test
    void getPDFController_success_coversTryBlock() throws Exception {
        PDFTestController controller = new PDFTestController();

        IPDFCreatorService pdfService = mock(IPDFCreatorService.class);
        ReflectionTestUtils.setField(controller, "pdfCreatorService", pdfService);

        Model model = new ExtendedModelMap();

        String view = controller.getPDFController(model, 1, 1);

        assertEquals("data-page", view);
        assertEquals("PDF izveidots veiksmīgi", model.getAttribute("package"));
        verify(pdfService, times(1)).createCertificateAsPDF(1, 1);
    }

    @Test
    void getPDFController_exception_coversCatchBlock() throws Exception {
        PDFTestController controller = new PDFTestController();

        IPDFCreatorService pdfService = mock(IPDFCreatorService.class);
        doThrow(new RuntimeException("Boom")).when(pdfService).createCertificateAsPDF(1, 1);
        ReflectionTestUtils.setField(controller, "pdfCreatorService", pdfService);

        Model model = new ExtendedModelMap();

        String view = controller.getPDFController(model, 1, 1);

        assertEquals("show-error-page", view);
        assertEquals("Boom", model.getAttribute("package"));
        verify(pdfService, times(1)).createCertificateAsPDF(1, 1);
    }
}
