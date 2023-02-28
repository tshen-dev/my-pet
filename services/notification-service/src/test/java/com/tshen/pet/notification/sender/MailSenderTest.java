package com.tshen.pet.notification.sender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tshen.pet.notification.sender.input.MailInput;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import jakarta.mail.Transport;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

class MailSenderTest {

  private final Environment environment = mock(Environment.class);

  @Test
  void whenSend_givenMailInput_ThenCallTransportToSendMessage() {
    when(environment.getProperty(anyString())).thenReturn("mockedValue");
    try (MockedStatic<Transport> transportMockedStatic = Mockito.mockStatic(Transport.class)) {
      MailInput mailInput = new MailInput();
      mailInput.setTo("petproject@tshen.com");

      new MailSender(environment).send(mailInput);

      transportMockedStatic.verify(() -> Transport.send(any()));
    }
  }

  @Test
  void whenSend_givenExceptionOccurWhenTransportSend_thenRethrowMyPetException() {
    when(environment.getProperty(anyString())).thenReturn("mockedValue");
    try (MockedStatic<Transport> transportMockedStatic = Mockito.mockStatic(Transport.class)) {
      MailInput mailInput = new MailInput();
      mailInput.setTo("petproject@tshen.com");
      transportMockedStatic.when(() -> Transport.send(any())).thenThrow(new RuntimeException());

      MailSender mailSender = new MailSender(environment);
      assertThrows(MyPetRuntimeException.class, () -> mailSender.send(mailInput));

      transportMockedStatic.verify(() -> Transport.send(any()));
    }
  }
}