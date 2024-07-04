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
  public List<UserDTO> getAllCustomers(@RequestParam(required = false)String searchParam) {
    return userService.getAllCustomers(searchParam);
  }

  @GetMapping("/{customerId}")
  public UserDTO getCustomerById(@PathVariable Long customerId) {
    return userService.findById(customerId);
  }

  @PostMapping("/{customerId}/set-discount/{discount}")
  public void setCustomerDiscount(@PathVariable Long customerId, @PathVariable Integer discount) {
    userService.setCustomerDiscount(customerId, discount);
  }

  @PostMapping("activate-user/{userId}")
  public void activateUser(@PathVariable Long userId){
    userService.activateUser(userId);
  }
}
