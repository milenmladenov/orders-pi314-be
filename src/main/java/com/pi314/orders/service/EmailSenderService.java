package com.pi314.orders.service;

import com.pi314.orders.model.entity.Order;
import com.pi314.orders.model.entity.User;
import jakarta.mail.*;

import java.io.*;

public interface EmailSenderService {

    void sendCreatedOrderEmail(Order order) throws MessagingException, UnsupportedEncodingException;
    void sendWorkingOnOrderEmail(Order order) throws MessagingException, UnsupportedEncodingException;
    void sendSentOrderEmail(Order order) throws MessagingException, UnsupportedEncodingException;

    void sendActivateUserRequest(User user) throws MessagingException, UnsupportedEncodingException;
    void sendUserIsActivatedEmail(User user);

}
