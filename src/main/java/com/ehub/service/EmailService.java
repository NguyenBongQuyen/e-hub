package com.ehub.service;

public interface EmailService {
    void send(String to, String subject, String body);
}
