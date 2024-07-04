package com.pi314.orders.service.impl;

import com.pi314.orders.exception.*;
import com.pi314.orders.model.dto.UserDTO;
import com.pi314.orders.model.entity.User;
import com.pi314.orders.repository.*;
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
  private final UserRepositoryCustom userRepositoryCustom;

  @Override
  public User register(UserDTO userInfo) {
    User user =
            User.builder().email(userInfo.getEmail()).username(userInfo.getUsername()).appliedDiscount(0).active(false).build();
    return userRepository.save(user);
  }

  @Override
  public boolean authenticate(String email, String password) {
    return false;
  }

  @Override
  public boolean isActivated(String username) {
    User user = getUserByUsername(username).orElseThrow();
      return user.isActive();
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
  public List<UserDTO> getAllCustomers(String searchParam) {
    if (searchParam == null){
      return modelMapperService.mapList(userRepository.findAll(), UserDTO.class);
    }
    List<User> users = userRepositoryCustom.filterUsers(searchParam);
    return modelMapperService.mapList(users, UserDTO.class);
  }

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public void activateUser(Long id) {
    User user = userRepository.findById(id).orElseThrow();
    user.setActive(true);
    userRepository.save(user);
  }

  @Override
  public void deleteUser(User user) {
    userRepository.delete(user);
  }

  @Override
  public UserDTO findById(Long customerId) {
    return modelMapperService.map(userRepository.findById(customerId).orElseThrow(), UserDTO.class);
  }

  @Override
  public void setCustomerDiscount(Long customerId, Integer discount) {
    User user = userRepository.findById(customerId).orElseThrow();
    user.setAppliedDiscount(discount);
    userRepository.save(user);
  }
}
