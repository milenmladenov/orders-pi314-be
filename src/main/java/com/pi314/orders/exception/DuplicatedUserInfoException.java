package com.pi314.orders.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedUserInfoException extends RuntimeException {

  public DuplicatedUserInfoException(String message) {
    super(message);
  }
    }