package com.domiaffaire.api.events.listener;


import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.events.RegistrationCompleteEvent;
import com.domiaffaire.api.services.jwt.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    private final JavaMailSender mailSender;
    private User theUser;
    private String url;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //1. Get the newly registered user
        theUser =  event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser, verificationToken);
        //4. Build the verification url to be sent to the user
        url = event.getApplicationUrl()+"/api/auth/verifyEmail?token="+verificationToken;
        //5. Send the email
//        try {
//            sendVerificationEmail(url);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
        log.info("Click the link to verify your registration: {} ",url);

        //6. send the sms
    }
    public void sendVerificationEmail() throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Domi Affaire";
//        String mailContent = "<p> Hi, "+ theUser.getUsername()+ ", </p>"+
//                "<p>Thank you for registering with us,"+"" +
//                "Please, follow the link below to complete your registration.</p>"+
//                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
//                "<p>Domi Affaire";

        // Create an adaptable email template
        String mailContent = "<html><body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">" +
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">" +
                "<tr><td style=\"background-color: #ffffff; padding: 20px;\">" +
                "<img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">" +
                "</td></tr>" +
                "<tr><td style=\"background-color: #ffffff; padding: 20px;\">" +
                "<h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Validation de l'adresse e-mail</h1>" +
                "<p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Bonjour, <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>" +
                "<p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous vous remercions de vous être inscrit chez nous ! Veuillez suivre le lien ci-dessous pour finaliser votre inscription :</p>" +
                "<p style=\"text-align: center; margin-top: 20px;\"><a href=\""+url+"\" style=\"display: inline-block; padding: 12px 24px; background-color: #ff8c00; color: #ffffff; text-decoration: none; border-radius: 5px;\">Valider votre e-mail pour activer votre compte</a></p>" +
                "<p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>L'équipe Domi Affaire</p>" +
                "</td></tr></table></div></body></html>";






        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendPasswordResetVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String url="http://localhost:4200/reset-password";
        String subject = "Password Reset Request Verification";
        String senderName = "Domi Affaire";
        String mailContent = "<html><body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">" +
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">" +
                "<tr><td style=\"background-color: #ffffff; padding: 20px;\">" +
                "<img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">" +
                "</td></tr>" +
                "<tr><td style=\"background-color: #ffffff; padding: 20px;\">" +
                "<h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Réinitialisation du mot de passe</h1>" +
                "<p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Bonjour, <strong>"+user.getFirstName()+" "+user.getLastName()+"</strong>,</p>" +
                "<p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Vous avez récemment demandé à réinitialiser votre mot de passe. Veuillez suivre le lien ci-dessous pour compléter l'action :</p>" +
                "<p style=\"text-align: center; margin-top: 20px;\"><a href=\""+url+"\" style=\"display: inline-block; padding: 12px 24px; background-color: #ff8c00; color: #ffffff; text-decoration: none; border-radius: 5px;\">Réinitialiser le mot de passe</a></p>" +
                "<p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>L'équipe Domi Affaire</p>" +
                "</td></tr></table></div></body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendDisableAccountMessage() throws MessagingException, UnsupportedEncodingException {
        String subject = "Warniiiiing!!!!!";
        String senderName = "Domi Affaire";
        String mailContent = "<p> Hello, "+ theUser.getUsername()+ ", </p>"+
                "<p><b>Your account has been disabled, </b>"+"" +
                "Bara khales !!!!!!!!!.</p>"+
                "<p>Domi Affaire";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendAcceptationDomiciliationRequest() throws MessagingException, UnsupportedEncodingException {
        String subject = "Domiciliation Request Accepted";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Domiciliation Request Accepted</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Domiciliation Request Accepted</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Hello, <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">I'm pleased to inform you that your request for domiciliation services has been accepted.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">We've also sent the draft contract to your account on our website. Please review it at your earliest convenience. We kindly ask you to confirm your acceptance of the terms outlined in the contract.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">If you have any questions or need further assistance, feel free to reach out. We're here to help.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Thank you for choosing DomiAffaire. We look forward to serving you.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Best regards,<br/>Imen,<br> DomiAffaire</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendRejectionDomiciliationRequest() throws MessagingException, UnsupportedEncodingException {
        String subject = "Domiciliation Request Rejected";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Domiciliation Request Rejected</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Domiciliation Request Rejected</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Hello, <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">I regret to inform you that after careful consideration, we are unable to accept your request for domiciliation services with DomiAffaire</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">We understand that this may be disappointing news, and we sincerely apologize for any inconvenience caused. If you have any questions or would like further clarification on our decision, please don't hesitate to reach out.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Thank you for considering DomiAffaire for your domiciliation needs. We wish you the best of luck with your future endeavors.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Best regards,<br/>Imen,<br> DomiAffaire</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendDeadlinePassedLimitWarning() throws MessagingException, UnsupportedEncodingException {
        String subject = "Me khalstech";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Me khalestech</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Me khalestech periode loula</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Hello, <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Rak me khalestch</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendDeadlineFiveDaysLeftWarning() throws MessagingException, UnsupportedEncodingException {
        String subject = "Be carefull";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Me khalestech</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Mezelo 5 ayem</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Hello, <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Rak me khalestch</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}

