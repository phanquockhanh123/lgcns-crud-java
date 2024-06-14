package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.emails.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
