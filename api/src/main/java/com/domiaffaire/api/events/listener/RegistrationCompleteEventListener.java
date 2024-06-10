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
        theUser =  event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser, verificationToken);
        url = event.getApplicationUrl()+"/api/auth/verifyEmail?token="+verificationToken;
        log.info("Click the link to verify your registration: {} ",url);
    }
    public void sendVerificationEmail() throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Domi Affaire";
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
        String subject = "Votre compte a été archivé en raison de non-paiement";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Votre compte a été archivé</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cher(e) <strong>" + theUser.getFirstName() + " " + theUser.getLastName() + "</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous regrettons de vous informer que votre compte a été archivé en raison du non-paiement de votre domiciliation.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Si vous souhaitez réactiver votre compte, veuillez régulariser votre situation en effectuant le paiement dès que possible.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Si vous avez des questions ou si vous avez besoin d'assistance supplémentaire, n'hésitez pas à nous contacter.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous vous remercions pour votre compréhension et espérons vous retrouver bientôt.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>Imen,<br> DomiAffaire</p>\n" +
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

    public void sendAcceptationDomiciliationRequest() throws MessagingException, UnsupportedEncodingException {
        String subject = "Demande de domiciliation acceptée";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Demande de domiciliation acceptée</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Demande de domiciliation acceptée</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Bonjour, <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Je suis heureuse de vous informer que votre demande de services de domiciliation a été acceptée.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous avons également envoyé le contrat provisoire à votre compte sur notre site Web. Veuillez le consulter dès que possible. Nous vous prions de bien vouloir confirmer votre acceptation des termes figurant dans le contrat.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Si vous avez des questions ou si vous avez besoin d'une assistance supplémentaire, n'hésitez pas à nous contacter. Nous sommes là pour vous aider.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Merci d'avoir choisi DomiAffaire. Nous avons hâte de vous servir.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>Imen,<br> DomiAffaire</p>\n" +
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
        String subject = "Demande de domiciliation refusée";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Demande de domiciliation refusée</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Demande de domiciliation refusée</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Bonjour, <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Je regrette de vous informer qu'après examen attentif, nous ne sommes pas en mesure d'accepter votre demande de services de domiciliation avec DomiAffaire.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous comprenons que cette nouvelle peut être décevante, et nous vous présentons nos sincères excuses pour tout désagrément causé. Si vous avez des questions ou si vous souhaitez obtenir des éclaircissements supplémentaires sur notre décision, n'hésitez pas à nous contacter.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Merci d'avoir envisagé DomiAffaire pour vos besoins en domiciliation. Nous vous souhaitons bonne chance dans vos projets futurs.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>Imen,<br> DomiAffaire</p>\n" +
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
        String subject = "Rappel : Limite de paiement dépassée";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Rappel : Limite de paiement dépassée</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Rappel : Limite de paiement dépassée</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cher(e) <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous tenons à vous informer que la limite de paiement de votre domiciliation a été dépassée. Veuillez procéder au paiement dès que possible</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Si vous avez des questions ou si vous avez besoin d'assistance supplémentaire, n'hésitez pas à nous contacter.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous vous remercions pour votre compréhension et votre coopération.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>Imen,<br> DomiAffaire</p>\n" +
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
        String subject = "Rappel : Il ne vous reste plus que 5 jours pour effectuer le paiement";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Rappel : Il ne vous reste plus que 5 jours pour effectuer le paiement</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cher(e) <strong>" + theUser.getFirstName() + " " + theUser.getLastName() + "</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous souhaitons vous rappeler qu'il ne vous reste plus que 5 jours pour effectuer le paiement de votre domiciliation. Veuillez procéder au paiement dès que possible.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Si vous avez des questions ou si vous avez besoin d'assistance supplémentaire, n'hésitez pas à nous contacter.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous vous remercions pour votre compréhension et votre coopération.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>Imen,<br> DomiAffaire</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendAcceptationReservation() throws MessagingException, UnsupportedEncodingException {
        String subject = "Demande de réservation acceptée";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Demande de réservation acceptée</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cher(e), <strong>"+theUser.getFirstName()+" "+theUser.getLastName()+"</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous avons le plaisir de vous informer que votre demande de réservation a été acceptée. Vous pouvez maintenant vérifier les détails de votre réservation sur notre site.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Si vous avez des questions ou si vous avez besoin d'assistance supplémentaire, n'hésitez pas à nous contacter.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous vous remercions pour votre confiance et nous réjouissons de vous accueillir prochainement.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>Imen,<br> DomiAffaire</p>\n" +
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

    public void sendRefusalReservation() throws MessagingException, UnsupportedEncodingException {
        String subject = "Demande de réservation refusée";
        String senderName = "Domi Affaire";
        String mailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <img src=\"https://domi-affaire.com/wp-content/uploads/2023/09/domi-affaire-creation-entreprise-tunis-ariana.png\" alt=\"Logo Domi Affaire\" style=\"display: block; max-width: 100%; height: auto; margin: 0 auto;\">\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #ffffff; padding: 20px;\">\n" +
                "            <h1 style=\"color: #333; font-size: 24px; margin-bottom: 20px;\">Demande de réservation refusée</h1>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cher(e) <strong>" + theUser.getFirstName() + " " + theUser.getLastName() + "</strong>,</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous regrettons de vous informer que votre demande de réservation a été refusée. Vous pouvez consulter notre site pour plus de détails ou pour effectuer une nouvelle demande.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Si vous avez des questions ou si vous avez besoin d'assistance supplémentaire, n'hésitez pas à nous contacter.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Nous vous remercions pour votre compréhension.</p>\n" +
                "            <p style=\"color: #555; font-size: 16px; line-height: 1.5;\">Cordialement,<br/>Imen,<br> DomiAffaire</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wassim.ouertani.25@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}

