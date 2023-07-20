package com.pi314.orders.service;

import com.pi314.orders.model.dto.UserDTO;
import com.pi314.orders.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register (UserDTO userInfo);

    boolean authenticate ( String email, String password);

    User findByEmail(String email);

}
