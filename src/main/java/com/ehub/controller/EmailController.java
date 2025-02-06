package com.ehub.controller;

import com.ehub.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "EMAIL-CONTROLLER")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/send-email")
    public void sendEmail(@RequestParam String to,
                          @RequestParam String subject,
                          @RequestParam String body) {
        log.info("Sending email to {}", to);
        emailService.send(to, subject, body);
    }
}
