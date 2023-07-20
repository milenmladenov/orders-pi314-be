package com.pi314.orders.service.impl;

import com.pi314.orders.model.dto.UserDTO;
import com.pi314.orders.model.entity.Role;
import com.pi314.orders.model.entity.User;
import com.pi314.orders.repository.UserRepository;
import com.pi314.orders.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("No user found with email: " + email);
    }

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
  }

  private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }

  @Override
  public User register(UserDTO userInfo) {
    User user =
        User.builder()
            .email(userInfo.getEmail())
            .password(passwordEncoder.encode(userInfo.getPassword()))
            .build();
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
}
