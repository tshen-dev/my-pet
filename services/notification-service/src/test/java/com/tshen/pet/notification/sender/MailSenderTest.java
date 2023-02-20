package com.tshen.pet.notification.sender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.tshen.pet.notification.sender.input.MailInput;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import jakarta.mail.Transport;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class MailSenderTest {

  private final MailSender instance = new MailSender();

  @Test
  void send_callTransportToSendMessage() {
    try (MockedStatic<Transport> transportMockedStatic = Mockito.mockStatic(Transport.class)) {
      MailInput mailInput = new MailInput();
      mailInput.setTo("tshen.petproject@gmail.com");

      instance.send(mailInput);

      transportMockedStatic.verify(() -> Transport.send(any()));
    }
  }

  @Test
  void send_callTransportToSendMessageWithExceptionOccur_rethrowMyPetException() {
    try (MockedStatic<Transport> transportMockedStatic = Mockito.mockStatic(Transport.class)) {
      MailInput mailInput = new MailInput();
      mailInput.setTo("tshen.petproject@gmail.com");
      transportMockedStatic.when(() -> Transport.send(any())).thenThrow(new RuntimeException());

      assertThrows(MyPetRuntimeException.class, () -> instance.send(mailInput));

      transportMockedStatic.verify(() -> Transport.send(any()));
    }
  }
}