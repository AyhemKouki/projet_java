package service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_FROM = dotenv.get("EMAIL_FROM");
    private static final String EMAIL_PASSWORD = dotenv.get("EMAIL_PASSWORD");

    public static void sendEmail(String recipientEmail, String subject, String body){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        Session session = Session.getInstance(props,new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("email has been sent to "+recipientEmail+" to remind him to return library item\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}