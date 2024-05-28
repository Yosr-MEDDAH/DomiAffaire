package com.domiaffaire.api.services;

import com.domiaffaire.api.entities.Deadline;
import com.domiaffaire.api.entities.DomiciliationRequest;
import com.domiaffaire.api.enums.PaymentMode;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.events.listener.RegistrationCompleteEventListener;
import com.domiaffaire.api.repositories.DeadlineRepository;
import com.domiaffaire.api.repositories.DomiciliationRequestRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadlineServiceImpl implements DeadlineService{
    private final DeadlineRepository deadlineRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final ApplicationEventPublisher publisher;
    private final DomiciliationRequestRepository domiciliationRequestRepository;



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
    public void sendEmailPaimentLimitWarning() throws MessagingException, UnsupportedEncodingException {
        eventListener.sendDeadlinePassedLimitWarning();
    }

    @Override
    public void sendEmailFiveDaysLeftWarning() throws MessagingException, UnsupportedEncodingException {
        eventListener.sendDeadlineFiveDaysLeftWarning();
    }
}
