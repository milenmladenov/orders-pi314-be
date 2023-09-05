package com.pi314.orders.service.impl;

import com.pi314.orders.service.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.*;
import lombok.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

  private String NEW_CREATED_ORDER_EMAIL_CONTENT = "";

  private final JavaMailSender javaMailSender;

  @Override
  public void sendCreatedOrderEmail(String recipientEmail)
      throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setFrom("kanbanna@abv.bg", "Kanban System Support");
    helper.setTo(recipientEmail);
    helper.setSubject("Нова поръчка");
    helper.setText(NEW_CREATED_ORDER_EMAIL_CONTENT, true);
    javaMailSender.send(message);
  }
}
