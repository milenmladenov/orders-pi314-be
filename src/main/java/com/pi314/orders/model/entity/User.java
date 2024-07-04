package com.pi314.orders.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @JsonIgnore private String username;
  @JsonIgnore private String email;

  @JsonIgnore private String password;
  private String companyAddress;
  private String communicationName;
  private String phone;
  private String companyName;
  private String bulstat;
  private String city;
  private boolean isDdsRegistered;
  private String mol;
  private Integer postCode;


  private String role;

  private Integer appliedDiscount;
  private boolean active;
  private String orderAddress;
  @OneToMany private List<Order> orders;
}
