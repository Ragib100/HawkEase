package me.hawkease;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class SendEmail {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String EMAIL_SUBJECT = "OTP for verification";

    public void sendEmail(String recipientEmail, String messageText) {
        String senderEmail = System.getenv("email_id");
        String senderPassword = System.getenv("email_password");
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(EMAIL_SUBJECT);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}