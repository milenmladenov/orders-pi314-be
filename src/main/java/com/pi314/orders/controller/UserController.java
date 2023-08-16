package com.pi314.orders.controller;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.service.*;
import java.util.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/customers")
public class UserController {

  private final UserService userService;

  @GetMapping
  public List<UserDTO> getAllCustomers() {
    return userService.getAllCustomers();
  }
}
