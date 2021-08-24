package com.store.project.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    public JavaMailSender javaMailSender;

    @Async
    public void sendMail(String email,String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");

        helper.setFrom("Shop");
        helper.setTo(email); //받는사람
        helper.setSubject(subject);
        helper.setText(message,true);

        javaMailSender.send(mimeMessage);

    }
}
