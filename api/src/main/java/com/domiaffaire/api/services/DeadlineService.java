package com.domiaffaire.api.services;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.UnsupportedEncodingException;

public interface DeadlineService {

    void checkDeadlines();

    void sendEmailPaimentLimitWarning() throws MessagingException, UnsupportedEncodingException;

    void sendEmailFiveDaysLeftWarning() throws MessagingException, UnsupportedEncodingException;
}
