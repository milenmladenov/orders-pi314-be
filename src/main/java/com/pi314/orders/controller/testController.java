package com.pi314.orders.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class testController {

  @GetMapping("/hello")
  public String hello() {
    return "hello";
}
}
