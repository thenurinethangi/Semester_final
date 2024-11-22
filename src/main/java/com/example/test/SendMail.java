package com.example.test;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {


    public void sendMail(String recipientEmail, String subject, String msg) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myEmail = "thenurinathangi@gmail.com";
        String password = "hkzu guyn ahxq hlmh";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO, new Address[]{
                    new InternetAddress(recipientEmail)
            });
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);

        }
        catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error while sending email to tenant: "+ e.getMessage());

        }
    }
}
