package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.DeadlineCountDTO;
import com.domiaffaire.api.dto.DeadlineDTO;
import com.domiaffaire.api.dto.DeadlineFilterRequest;
import com.domiaffaire.api.entities.Deadline;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

public interface DeadlineService {

    void checkDeadlines();
    List<DeadlineDTO> getDeadlinesBetweenTwoDates(DeadlineFilterRequest request);
    List<DeadlineDTO> getDeadlinesBetweenTwoDatesFixed();

    void sendEmailPaimentLimitWarning() throws MessagingException, UnsupportedEncodingException;

    void sendEmailFiveDaysLeftWarning() throws MessagingException, UnsupportedEncodingException;
    List<DeadlineDTO> getDeadlinesOfClientAuthenticated()throws UserNotFoundException;
    List<DeadlineDTO> getDeadlinesOfClientById(String id)throws UserNotFoundException;

    List<DeadlineCountDTO> getDeadlineDistribution();
    String getDelayRange(Deadline deadline);
}
