package com.pi314.orders.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {
  private Long id;
  @NotEmpty private String email;
  private String username;
  private String phone;
  private String companyName;
  private String companyAddress;

  private String communicationName;
  private String bulstat;
  private String city;
  private boolean isDdsRegistered;
  private String mol;
  private Integer postCode;
}
