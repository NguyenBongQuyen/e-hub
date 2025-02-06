package com.ehub.service.impl;

import com.ehub.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j(topic = "EMAIL-SERVICE")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${spring.sendgrid.from}")
    private String from;

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
}
