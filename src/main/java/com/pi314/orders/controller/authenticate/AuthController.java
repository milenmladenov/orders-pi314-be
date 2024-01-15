package com.pi314.orders.controller.authenticate;

import com.pi314.orders.config.*;
import com.pi314.orders.exception.*;
import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

  @PostMapping("/authenticate")
  public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
    String token = authenticateAndGetToken(loginRequest.getUsername(), loginRequest.getPassword());
    return new AuthResponse(token);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/signup")
  public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userService.hasUserWithUsername(signUpRequest.getUsername())) {
      throw new DuplicatedUserInfoException(
          String.format("Username %s already been used", signUpRequest.getUsername()));
    }
    if (userService.hasUserWithEmail(signUpRequest.getEmail())) {
      throw new DuplicatedUserInfoException(
          String.format("Email %s already been used", signUpRequest.getEmail()));
    }

    userService.saveUser(mapSignUpRequestToUser(signUpRequest));

    String token =
        authenticateAndGetToken(signUpRequest.getUsername(), signUpRequest.getPassword());
    return new AuthResponse(token);
  }

  private String authenticateAndGetToken(String username, String password) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
    return tokenProvider.generate(authentication);
  }

  private User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
    User user = new User();
    user.setUsername(signUpRequest.getUsername());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    user.setCompanyName(signUpRequest.getCompanyName());
    user.setEmail(signUpRequest.getEmail());
    user.setCity(signUpRequest.getCity());
    user.setBulstat(signUpRequest.getBulstat());
    user.setMol(signUpRequest.getMol());
    user.setPhone(signUpRequest.getPhone());
    user.setCommunicationName(signUpRequest.getCommunicationName());
    user.setDdsRegistered(signUpRequest.isDdsRegistered());
    user.setOrderAddress(signUpRequest.getOrderAddress());
    user.setPostCode(signUpRequest.getPostCode());
    user.setCompanyAddress(signUpRequest.getCompanyAddress());
    user.setAppliedDiscount(0);

    if (signUpRequest.getUsername().equals("oadmin")) {
      user.setRole(SecurityConfig.ADMIN);
    } else {
      user.setRole(SecurityConfig.USER);
    }
    return user;
  }
}
