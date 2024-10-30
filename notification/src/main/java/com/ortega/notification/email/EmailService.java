package com.ortega.notification.email;

import com.ortega.notification.event.account.AccountStatusUpdatedEvent;
import com.ortega.notification.event.customer.CustomerCreatedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static com.ortega.notification.email.EmailTemplate.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendAccountStatusUpdatedNotification(AccountStatusUpdatedEvent event) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                UTF_8.name()
        );

        final String templateName = event.getStatus() ?
                ACTIVATION_ACCOUNT_NOTIFICATION.getTemplate():
                DEACTIVATION_ACCOUNT_NOTIFICATION.getTemplate();

        mimeMessageHelper.setSubject(
                event.getStatus() ?
                        ACTIVATION_ACCOUNT_NOTIFICATION.getSubject():
                        DEACTIVATION_ACCOUNT_NOTIFICATION.getSubject()
                );

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", String.format("%s %s", event.getFirstName(), event.getLastName()));


        Context context = new Context();
        context.setVariables(variables);

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate, true);

            mimeMessageHelper.setTo(event.getEmail());
            javaMailSender.send(mimeMessage);

            log.info(String.format("Email successfully sent to %s" , event.getEmail()));
        } catch (MessagingException e) {
            log.warn("Can not send account status updated notification to {}", e.getMessage());
        }
    }

    @Async
    public void sendCustomerCreatedNotification(CustomerCreatedEvent event) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                UTF_8.name()
        );

        final String templateName = CREATION_ACCOUNT_NOTIFICATION.getTemplate();
        mimeMessageHelper.setSubject(CREATION_ACCOUNT_NOTIFICATION.getSubject());

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", String.format("%s %s", event.getFirstName(), event.getLastName()));
        variables.put("accountNumber", event.getAccountNumber());
        variables.put("pin", event.getPin());

        Context context = new Context();
        context.setVariables(variables);

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate, true);

            mimeMessageHelper.setTo(event.getEmail());
            javaMailSender.send(mimeMessage);

            log.info(String.format("Email successfully sent to %s" , event.getEmail()));
        } catch (MessagingException e) {
            log.warn("Cannot send customer created notification to {}", e.getMessage());
        }
    }
}
