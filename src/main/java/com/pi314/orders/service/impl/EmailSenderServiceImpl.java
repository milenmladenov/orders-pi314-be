package com.pi314.orders.service.impl;

import com.pi314.orders.model.entity.Order;
import com.pi314.orders.model.entity.User;
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

    private String NEW_CREATED_ORDER_EMAIL_CONTENT = "<p>Здравейте,<p>\n" +
                                                     "\n" +
                                                     "<p>Поръчка с No.%s от дата: %s за изработване на вратички е изпратена " +
                                                     "успешно за обработка и ще бъде потвърдена в рамките на един работен ден" +
                                                     ".<p>\n" +
                                                     "\n" +
                                                     "<p><a href=\"https://orders-pi314.netlify" +
                                                     ".app/app/orders/%s#pdf-button\">Изтегли PDF<a><p>\n" +
                                                     "\n" +
                                                     "<p><a href=\"https://orders-pi314.netlify.app/app/orders/%s\">Към " +
                                                     "поръчката<a><p>";

    private static final String ORDER_WITH_WORKING_ON_STATUS_EMAIL_CONTENT = "<p>Здравейте,<p>\n" +
                                                                             "\n" +
                                                                             "<p>Поръчка с No.%s от дата: %s вече се изпълнява " +
                                                                             "и не са възможни корекции по нея. Очаквайте " +
                                                                             "вратичките да бъдат доставени на посочения от Вас" +
                                                                             " адрес на %s.<p>\n" +
                                                                             "\n" +
                                                                             "<p><a href=\"https://orders-pi314.netlify" +
                                                                             ".app/app/orders/%s#pdf-button\">Изтегли " +
                                                                             "PDF<a><p>\n" +
                                                                             "\n" +
                                                                             "<p><a href=\"https://orders-pi314.netlify" +
                                                                             ".app/app/orders/%s\">Към поръчката<a><p>";

    private String ORDER_WITH_DONE_STATUS_EMAIL_CONTENT = "<p>Здравейте,<p>\n" +
                                                          "\n" +
                                                          "<p>Поръчка с No.%s от дата: %s е изпълнена и ще бъде доставена на " +
                                                          "посочения от вас адрес за доставка през утрешния ден.<p>\n" +
                                                          "\n" +
                                                          "<p><a href=\"https://orders-pi314.netlify" +
                                                          ".app/app/orders/%s#pdf-button\">Изтегли PDF<a><p>\n" +
                                                          "\n" +
                                                          "<p><a href=\"https://orders-pi314.netlify.app/app/orders/%s\">Към " +
                                                          "поръчката<a><p>";
    private String NEW_CUSTOMER_TO_ACTIVAТE_EMAIL_CONTENT = """
            Здравейте,                           \s
            Имате нов регистриран клиент %s, който очаква активиране на профила!    \s
            Може да го активирате в секция Клиенти !""";

    private String NEW_CUSTOMER_IS_ACTIVATED_EMAIL_CONTENT = """
                  Здравейте %s,                           \s
                  Вашият профил е активиран и вече имате достъп до системата за поръчки на ПИ 314!    \s
            """;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendCreatedOrderEmail(Order order)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("pi314", "Пи314");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject(String.format("Поръчка с No.%s за изработване на вратички е изпратена успешно", order.getOrderUuid()));
        helper.setText(String.format(NEW_CREATED_ORDER_EMAIL_CONTENT, order.getOrderUuid(), order.getCreatedAt(), order.getId(),
                order.getId()), true);
        javaMailSender.send(message);
    }

    @Override
    public void sendWorkingOnOrderEmail(Order order) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("pi314", "Пи314");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject(String.format("Поръчка с No.%s за изработване на вратички вече се изпълнява.", order.getOrderUuid()));
        helper.setText(String.format(ORDER_WITH_WORKING_ON_STATUS_EMAIL_CONTENT, order.getOrderUuid(), order.getCreatedAt(),
                order.getCreatedAt().plusWeeks(3), order.getId(), order.getId()), true);
        javaMailSender.send(message);
    }

    @Override
    public void sendSentOrderEmail(Order order) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("pi314", "Пи314");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject(String.format("Поръчка с No.%s за изработване на вратички вече е изпълнена.", order.getOrderUuid()));
        helper.setText(
                String.format(ORDER_WITH_DONE_STATUS_EMAIL_CONTENT, order.getOrderUuid(), order.getCreatedAt(), order.getId(),
                        order.getId()), true);
        javaMailSender.send(message);

    }

    @Override
    public void sendActivateUserRequest(User user) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("pi314", "Пи314");
        helper.setTo("milen.mladenov.94@gmail.com");
        helper.setSubject(String.format("Нов регистриран потребител %s", user.getCompanyName()));
        helper.setText(String.format(NEW_CUSTOMER_TO_ACTIVAТE_EMAIL_CONTENT, user.getCompanyName()));
        javaMailSender.send(message);

    }

    @Override
    public void sendUserIsActivatedEmail(User user) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("pi314", "Пи314");
        helper.setTo(user.getEmail());
        helper.setSubject("Активиране на профила");
        helper.setText(String.format(NEW_CUSTOMER_IS_ACTIVATED_EMAIL_CONTENT, user.getCompanyName()));
        javaMailSender.send(message);
    }
}
