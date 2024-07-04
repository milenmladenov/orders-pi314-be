package com.pi314.orders.service;

import com.pi314.orders.model.dto.UserDTO;
import com.pi314.orders.model.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.UnsupportedEncodingException;
import java.util.*;

public interface UserService{

    User register (UserDTO userInfo);

    boolean authenticate ( String email, String password);

    boolean isActivated(String username);

    User findByEmail(String email);

    Optional<User> getLoggedUser();

    List<User> getUsers();

    Optional<User> getUserByUsername(String username);

    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    User validateAndGetUserByUsername(String username);

    List<UserDTO> getAllCustomers(String searchParam);

    void saveUser(User user) throws MessagingException, UnsupportedEncodingException;

    void activateUser(Long id) throws MessagingException, UnsupportedEncodingException;

    void deleteUser(Long userId);

    UserDTO findById(Long customerId);

    void setCustomerDiscount(Long customerId, Integer discount);
}
