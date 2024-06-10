package com.domiaffaire.api.services;

import ch.qos.logback.core.net.server.Client;
import com.domiaffaire.api.dto.DeadlineCountDTO;
import com.domiaffaire.api.dto.DeadlineDTO;
import com.domiaffaire.api.dto.DeadlineFilterRequest;
import com.domiaffaire.api.dto.FileDTO;
import com.domiaffaire.api.entities.Deadline;
import com.domiaffaire.api.entities.DomiciliationRequest;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.PaymentMode;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.events.listener.RegistrationCompleteEventListener;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.DeadlineRepository;
import com.domiaffaire.api.repositories.DomiciliationRequestRepository;
import com.domiaffaire.api.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadlineServiceImpl implements DeadlineService{
    private final DeadlineRepository deadlineRepository;
    private final UserRepository userRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final ApplicationEventPublisher publisher;
    private final DomiciliationRequestRepository domiciliationRequestRepository;
    private final Mapper mapper;



    @Override
    @Scheduled(fixedRate = 1000)
    public void checkDeadlines() {
        List<DomiciliationRequest> requests = domiciliationRequestRepository.findAllByDeadlineNotNull();
        LocalDateTime now = LocalDateTime.now();

        for (DomiciliationRequest request : requests) {
            Deadline deadline = request.getDeadline();
            if (deadline != null && !request.isEmailSent()) {
                LocalDateTime limitedDate = deadline.getLimitedDate();

                // Check if it's 5 days before the limited date
//                limitedDate.minusDays(5).isBefore(now) && limitedDate.isAfter(now)
                if (now.compareTo(limitedDate.minusDays(5)) > 0 && !request.isEmailSent()) {
                    log.info("Reminder: Payment Due Your payment is due in 5 days.");
                    request.setEmailSent(true);
                    publisher.publishEvent(new RegistrationCompleteEvent(request.getClient()));
                    domiciliationRequestRepository.save(request);
                    try {
                        sendEmailFiveDaysLeftWarning();
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    request.setEmailSent(false);

                }
//                if limitedDate.isBefore(now)
                // Check if the limited date has passed
                if (now.compareTo(limitedDate) > 0 && !request.isEmailSent()) {
                    log.info("Alert: Payment Overdue Your payment is overdue.");
                    request.setEmailSent(true);
                    publisher.publishEvent(new RegistrationCompleteEvent(request.getClient()));

                    request.setEmailSent(true);
                    if (request.getPaymentMode() == PaymentMode.QUARTER) {
                        request.getDeadline().setDateBeginig(request.getDeadline().getDateBeginig().plusMonths(3));
                        request.getDeadline().setLimitedDate(request.getDeadline().getLimitedDate().plusMonths(3));
                    } else if (request.getPaymentMode() == PaymentMode.SEMESTER) {
                        request.getDeadline().setDateBeginig(request.getDeadline().getDateBeginig().plusMonths(6));
                        request.getDeadline().setLimitedDate(request.getDeadline().getLimitedDate().plusMonths(6));
                    } else if (request.getPaymentMode() == PaymentMode.ANNUALLY) {
                        request.getDeadline().setDateBeginig(request.getDeadline().getDateBeginig().plusMonths(12));
                        request.getDeadline().setLimitedDate(request.getDeadline().getLimitedDate().plusMonths(12));
                    }
                    deadline.setCounterOfNotPaidPeriods(deadline.getCounterOfNotPaidPeriods() + 1);
                    request.setEmailSent(false);
                    deadlineRepository.save(request.getDeadline());
                    domiciliationRequestRepository.save(request);
                    try {
                        sendEmailPaimentLimitWarning();
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    request.setEmailSent(false);
                }
            }
        }
    }

    @Override
    public List<DeadlineDTO> getDeadlinesBetweenTwoDates(DeadlineFilterRequest request) {
        List<Deadline> deadlines = deadlineRepository.findAllByLimitedDateBetween(request.getDate1(),request.getDate2());
        List<DeadlineDTO> deadlineDTOS = new ArrayList<>();
        for(Deadline deadline:deadlines){
            User client = domiciliationRequestRepository.findByDeadline(deadline).getClient();
            DeadlineDTO deadlineDTO= mapper.fromDeadlineToDeadlineDTO(deadline);
            deadlineDTO.setClient(client);
            deadlineDTOS.add(deadlineDTO);
        }
        return deadlineDTOS;
    }

    @Override
    public List<DeadlineDTO> getDeadlinesBetweenTwoDatesFixed() {
        List<Deadline> deadlines = deadlineRepository.findAllByLimitedDateBetween(LocalDateTime.now(),LocalDateTime.now().plusDays(14));
        List<DeadlineDTO> deadlineDTOS = new ArrayList<>();
        for(Deadline deadline:deadlines){
            User client = domiciliationRequestRepository.findByDeadline(deadline).getClient();
            DeadlineDTO deadlineDTO= mapper.fromDeadlineToDeadlineDTO(deadline);
            deadlineDTO.setClient(client);
            deadlineDTOS.add(deadlineDTO);
        }
        return deadlineDTOS;
    }

    @Override
    public void sendEmailPaimentLimitWarning() throws MessagingException, UnsupportedEncodingException {
        eventListener.sendDeadlinePassedLimitWarning();
    }

    @Override
    public void sendEmailFiveDaysLeftWarning() throws MessagingException, UnsupportedEncodingException {
        eventListener.sendDeadlineFiveDaysLeftWarning();
    }

    @Override
    public List<DeadlineDTO> getDeadlinesOfClientAuthenticated() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        List<Deadline> deadlines = new ArrayList<>();
        List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByClient(authenticatedUser);
        for(DomiciliationRequest elmt:domiciliationRequests){
            if(elmt.getDeadline()!=null){
                deadlines.add(elmt.getDeadline());
            }
        }
        List<DeadlineDTO> deadlineDTOS = deadlines.stream()
                .map(deadline->mapper.fromDeadlineToDeadlineDTO(deadline))
                .collect(Collectors.toList());
        return deadlineDTOS;
    }

    @Override
    public List<DeadlineDTO> getDeadlinesOfClientById(String id) throws UserNotFoundException {
        Optional<User> client = userRepository.findById(id);
        if(client.isPresent()){
            List<Deadline> deadlines = new ArrayList<>();
            List<DomiciliationRequest> domiciliationRequests = domiciliationRequestRepository.findAllByClient(client.get());
            for(DomiciliationRequest elmt:domiciliationRequests){
                if(elmt.getDeadline()!=null){
                    deadlines.add(elmt.getDeadline());
                }
            }
            List<DeadlineDTO> deadlineDTOS = deadlines.stream()
                    .map(deadline->mapper.fromDeadlineToDeadlineDTO(deadline))
                    .collect(Collectors.toList());
            return deadlineDTOS;
        }else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<DeadlineCountDTO> getDeadlineDistribution() {
        List<Deadline> deadlines = deadlineRepository.findAll();
        Map<String, Long> distribution = deadlines.stream()
                .collect(Collectors.groupingBy(this::getDelayRange, Collectors.counting()));

        return distribution.entrySet().stream()
                .map(entry -> new DeadlineCountDTO(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String getDelayRange(Deadline deadline) {
        long daysBetween = ChronoUnit.DAYS.between(LocalDateTime.now(), deadline.getLimitedDate());
        if (daysBetween <= 7) {
            return "7 jours";
        } else if (daysBetween <= 15) {
            return "15 jours";
        } else if (daysBetween <= 30) {
            return "30 jours";
        } else {
            return ">30 jours";
        }
    }
}
