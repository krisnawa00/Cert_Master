package lv.venta.controller;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import lv.venta.service.impl.EmailSendingServiceImpl;

class EmailControllerTest {

    @Test
    void getEmailSend_coversServiceCallModelAndReturn() {
        EmailController controller = new EmailController();

        EmailSendingServiceImpl mockService = mock(EmailSendingServiceImpl.class);
        // override the "new EmailSendingServiceImpl()" field with our mock
        ReflectionTestUtils.setField(controller, "emailService", mockService);

        Model model = new ExtendedModelMap();

        String view = controller.getEmailSend(model);

        assertEquals("data-page", view);
        assertEquals("Epasts uz s24pekskris@venta.lv ir nosūtīts", model.getAttribute("package"));

        verify(mockService, times(1)).sendSimpleMsg(
                eq("s24pekskris@venta.lv"),
                eq("kristers1906@gmail.com"),
                eq("TDL School sertifikāts"),
                eq("Sveiki, šo sūta jums Kristers no TDL School. Jūs esat izdarijis visus kursus. Un pielikumā nosūtu Jūsu TDL School sertifikātu."),
                any(File.class)
        );
    }
}
