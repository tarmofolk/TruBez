package com.tarmofolk.trubez.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// класс для рассылки сообщений
@Service
public class MailSender {

   /**
    *  берем класс, который отвечает за действие с сообщениями*/
   @Autowired
    private JavaMailSender mailSender;

   /**
    * из пропертей берем того, ето будет отправлять ссообщение, скопировано из мэил конфига*/
    @Value("${spring.mail.username}")
    private String username;

   /**  метод, который рассылает почту, принимает на вход адресата, тему и тело письма*/
    public void send(String emailTo, String subject, String message) {

       /**  инстанциируем спринговый класс который отвечает за формирование сообщения*/
        SimpleMailMessage mailMessage = new SimpleMailMessage();
            // вызываем методы от кого, кому, тему и текст сообщения
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        // класс, обрабатывающий сообщения отправляет с, сфрмированное формирователем
        mailSender.send(mailMessage);
    }
}
