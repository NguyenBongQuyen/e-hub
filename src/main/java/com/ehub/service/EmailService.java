package com.ehub.service;

import java.io.IOException;

public interface EmailService {
    void send(String to, String subject, String body);

    void sendVerificationEmail(String to, String name) throws IOException;
}
