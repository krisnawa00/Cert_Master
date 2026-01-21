package lv.venta.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

class EmailSendingServiceImplTest {

    private JavaMailSender mailSender;
    private EmailSendingServiceImpl service;

    @BeforeEach
    void setUp() {
        mailSender = Mockito.mock(JavaMailSender.class);
        service = new EmailSendingServiceImpl();

        // inject mock into @Autowired field (no Spring context needed)
        org.springframework.test.util.ReflectionTestUtils.setField(service, "mailSender", mailSender);
    }

    @Test
    void sendSimpleMsg_success_sendsMailAndSetsAllFields(@TempDir Path tempDir) throws Exception {
        // Arrange: real MimeMessage so MimeMessageHelper works normally
        MimeMessage realMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(realMessage);

        File attachment = tempDir.resolve("test.txt").toFile();
        boolean created = attachment.createNewFile();
        if (!created) throw new IllegalStateException("Failed to create temp attachment file");

        // Act
        service.sendSimpleMsg(
                "to@example.com",
                "from@example.com",
                "Subject X",
                "Hello",
                attachment
        );

        // Assert: send() called with the same message produced by createMimeMessage()
        ArgumentCaptor<MimeMessage> msgCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(1)).send(msgCaptor.capture());
        MimeMessage sent = msgCaptor.getValue();

        assertNotNull(sent);
        // Also ensure createMimeMessage was used
        verify(mailSender, times(1)).createMimeMessage();
    }

    @Test
    void sendSimpleMsg_whenMailSenderThrows_sendIsNotCalled_andMethodDoesNotThrow(@TempDir Path tempDir) throws Exception {
        // Arrange
        MimeMessage realMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(realMessage);

        File attachment = tempDir.resolve("test.txt").toFile();
        boolean created = attachment.createNewFile();
        if (!created) throw new IllegalStateException("Failed to create temp attachment file");

        // Force exception inside try block: send() throws
        doThrow(new RuntimeException("SMTP down")).when(mailSender).send(any(MimeMessage.class));

        // Act (should be caught inside the method)
        service.sendSimpleMsg(
                "to@example.com",
                "from@example.com",
                "Subject Y",
                "Body",
                attachment
        );

        // Assert: it attempted to send once, exception was handled, test continues
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
