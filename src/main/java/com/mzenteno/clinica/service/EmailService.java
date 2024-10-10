package com.mzenteno.clinica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mzenteno.clinica.error.exception.BadRequestException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender mailSender;

  public void sendEmail(String to, String subject, String htmlBody){
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

      message.setFrom("clinica_zenteno@gmail.com");
      message.setTo(to);
      message.setSubject(subject);
      message.setText(htmlBody, true);

      mailSender.send(mimeMessage);      
    } catch (MessagingException e) {
      throw new BadRequestException("Error al enviar el correo electrónico");
    }
  }
}
