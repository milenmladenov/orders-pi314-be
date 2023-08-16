package com.pi314.orders.service;

import com.pi314.orders.model.dto.UserDTO;
import com.pi314.orders.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.*;

public interface UserService{

    User register (UserDTO userInfo);

    boolean authenticate ( String email, String password);

    User findByEmail(String email);

    Optional<User> getLoggedUser();

    List<User> getUsers();

    Optional<User> getUserByUsername(String username);

    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    User validateAndGetUserByUsername(String username);

    List<UserDTO> getAllCustomers();

    User saveUser(User user);

    void deleteUser(User user);

}
