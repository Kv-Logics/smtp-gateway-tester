package com.smtp;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@RestController
public class EmailController {

    @PostMapping("/send")
    public String send(@RequestBody EmailRequest req) {

        try {

            Properties props = new Properties();

            props.put("mail.smtp.host", req.host);
            props.put("mail.smtp.port", req.port);

            props.put("mail.smtp.auth", "true");

            // SSL SMTP (Port 465)
            props.put("mail.smtp.ssl.enable", "true");

            // Debug logs
            props.put("mail.debug", "true");

            Session session = Session.getInstance(
                    props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    req.username,
                                    req.password
                            );
                        }
                    });

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(req.username));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(req.to)
            );

            message.setSubject(req.subject);

            message.setContent(
                    req.body,
                    "text/html; charset=UTF-8"
            );

            Transport.send(message);

            return "SUCCESS";

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}