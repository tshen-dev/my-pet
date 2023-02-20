package com.tshen.pet.notification.sender;

import com.tshen.pet.notification.sender.input.MailInput;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailSender implements NotificationSender<MailInput> {

  private final Session session;
  private final String fromEmail;

  public MailSender() {
    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.auth", true);

    fromEmail = "tshen.petproject@gmail.com";
    var password = "zjvhlzzzdejabrwb";

    session = Session.getInstance(prop, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(fromEmail, password);
      }
    });
  }

  @Override
  public void send(MailInput input) {
    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(fromEmail));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(input.getTo()));
      message.setSubject(input.getTitle());

      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(input.getContent(), "text/html; charset=utf-8");

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(mimeBodyPart);

      message.setContent(multipart);

      log.info("Sending email to: {}", input.getTo());
      Transport.send(message);
      log.info("Send email success to: {}", input.getTo());
    } catch (Exception ex) {
      log.error("Could not send mail", ex);
      throw new MyPetRuntimeException();
    }
  }
}
