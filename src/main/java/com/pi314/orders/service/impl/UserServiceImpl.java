package com.pi314.orders.service.impl;

import com.pi314.orders.exception.*;
import com.pi314.orders.model.dto.UserDTO;
import com.pi314.orders.model.entity.User;
import com.pi314.orders.repository.UserRepository;
import com.pi314.orders.service.*;

import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final ModelMapperService modelMapperService;

  @Override
  public User register(UserDTO userInfo) {
    User user =
        User.builder()
            .email(userInfo.getEmail())
            .username(userInfo.getUsername()).build();
    return userRepository.save(user);
  }

  @Override
  public boolean authenticate(String email, String password) {
    return false;
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public Optional<User> getLoggedUser() {
    return userRepository.findByUsername(
        SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public boolean hasUserWithUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public boolean hasUserWithEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public User validateAndGetUserByUsername(String username) {
    return getUserByUsername(username)
        .orElseThrow(
            () ->
                new UserNotFoundException(
                    String.format("User with username %s not found", username)));
  }

  @Override
  public List<UserDTO> getAllCustomers() {
    return modelMapperService.mapList(userRepository.findAll(), UserDTO.class);
  }

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(User user) {
    userRepository.delete(user);
  }
}
