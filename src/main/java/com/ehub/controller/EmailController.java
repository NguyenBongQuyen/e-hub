package com.ehub.controller;

import com.ehub.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/emails")
@Slf4j(topic = "EMAIL-CONTROLLER")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @Operation(summary = "Send Email", description = "Send email for account")
    @GetMapping("/send-email")
    public void sendEmail(@RequestParam String to,
                          @RequestParam String subject,
                          @RequestParam String body) {
        log.info("Sending email to {}", to);
        emailService.send(to, subject, body);
    }

    @Operation(summary = "Verification Email", description = "Verification email for account")
    @PostMapping("/send-verification-email")
    public void sendVerificationEmail(@RequestParam String to,
                                      @RequestParam String name) {
        try {
            emailService.sendVerificationEmail(to, name);
            log.info("Verification email sent successfully!");
        } catch (Exception e) {
            log.info("Failed to send verification email.");
        }
    }

    @Operation(summary = "Confirm Email", description = "Confirm email for account")
    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String secretCode, HttpServletResponse response) throws IOException {
        log.info("Confirm email for account with secretCode: {}", secretCode);

        try {
            System.out.println("TODO check or compare secret code from database");
            // check or compare secret code from database
        } catch (Exception e) {
            log.error("Verification fail, error: {}", e.getMessage());
        } finally {
            response.sendRedirect("https://practicetestautomation.com/practice-test-login/");
        }
    }
}
