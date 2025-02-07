package com.ehub.service.impl;

import com.ehub.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j(topic = "EMAIL-SERVICE")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${spring.sendgrid.from}")
    private String from;

    @Value("${spring.sendgrid.verify-link}")
    private String verifyLink;

    @Value("${spring.sendgrid.template-id}")
    private String templateId;

    private final SendGrid sendGrid;

    @Override
    public void send(String to, String subject, String body) {
        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            // Kiểm tra kết quả phản hồi từ SendGrid
            if (response.getStatusCode() == 202) {
                log.info("Email sent successfully");
            } else {
                log.error("Email sent failed" + response.getBody());
            }
        } catch (IOException e) {
            log.error("Error occurred while sending email, error: {}", e.getMessage());
        }
    }

    @Override
    public void sendVerificationEmail(String to, String name) throws IOException {
        log.info("Sending verification email for name={}", name);

        Email fromEmail = new Email(from, "Newbie Learn Java");
        Email toEmail = new Email(to);
        String subject = "Account Verification";

        // Generate secret code and save to db
        String secretCode = UUID.randomUUID().toString();
        log.info("secretCode = {}", secretCode);

        // TOD0 save secretCode to db

        // Tạo dynamic template data
        Map<String, String> dynamicTemplateData = new HashMap<>();
        dynamicTemplateData.put("name", name);
        dynamicTemplateData.put("verify-link", verifyLink + "?secretCode=" + secretCode);

        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.setSubject(subject);

        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);

        // Add dynamic template data
        dynamicTemplateData.forEach(personalization::addDynamicTemplateData);

        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId); // TemplateID từ SendGrid

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        if (response.getStatusCode() == 202) {
            log.info("Verification sent successfully");
        } else {
            log.error("Verification sent failed");
        }
    }
}
