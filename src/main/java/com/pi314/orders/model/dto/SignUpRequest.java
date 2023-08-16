package com.pi314.orders.model.dto;

import com.fasterxml.jackson.annotation.*;
import com.pi314.orders.model.entity.*;
import java.util.*;
import lombok.Data;

@Data
public class SignUpRequest {
  private String username;
  private String email;

  private String password;
  private String companyAddress;
  private String communicationName;
  private String phone;
  private String companyName;
  private String bulstat;
  private String city;
  private boolean isDdsRegistered;
  private String mol;
  private Integer postCode;
  private List<OrderAddress> orderAddressList;
}
