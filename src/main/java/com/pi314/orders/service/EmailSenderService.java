package com.pi314.orders.service;

import jakarta.mail.*;

import java.io.*;

public interface EmailSenderService {

    void sendCreatedOrderEmail(String recipientEmail) throws MessagingException, UnsupportedEncodingException;
}
