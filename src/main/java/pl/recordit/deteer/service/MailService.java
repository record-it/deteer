package pl.recordit.deteer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.recordit.deteer.controll.Feedback;
import pl.recordit.deteer.entity.User;

import java.util.Optional;

@Service
public class MailService {
  private final JavaMailSender mailSender;
  private final VerifyingTokenService verifyingTokenService;

  @Autowired
  public MailService(JavaMailSender mailSender, VerifyingTokenService verifyingTokenService) {
    this.mailSender = mailSender;
    this.verifyingTokenService = verifyingTokenService;
  }

  public void sendMailTo(String email, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("deteer@record-it.pl");
    message.setTo(email);
    message.setSubject(subject);
    message.setText(content);
    mailSender.send(message);
  }

  public Feedback sendVerifyingMail(String email, String verifyingLink) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("deteer@record-it.pl");
      message.setTo(email);
      message.setSubject("Weryfikacja konta w DETEER");
      message.setText("Kliknik na link, aby uaktywnić zarejestrowanego użytkownika w aplikacji DETEER: " + verifyingLink);
      mailSender.send(message);
      return Feedback.ofSuccess();
    } catch (MailSendException e) {
      return Feedback.ofError(e.getMessage());
    }
  }
}
