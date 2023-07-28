package com.cafe.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMain(String to, String subject, String text, List<String> ccList) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("vrushant28@gmail.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);
        if (ccList != null && ccList.size() > 0) mail.setCc(getCcArray(ccList));
        mailSender.send(mail);
    }

    private String[] getCcArray(List<String> ccList) {
        String[] ccArray = new String[ccList.size()];
        int i = 0;
        for (String cc : ccList) {
            ccArray[i++] = cc;
        }
        return ccArray;
    }

    public void forgotMail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("vrushant28@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg, "text/html");
        mailSender.send(message);
    }
}
