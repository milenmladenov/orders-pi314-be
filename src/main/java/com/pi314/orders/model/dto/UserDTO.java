package com.pi314.orders.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {
  @NotEmpty private String email;

  @NotEmpty private String password;
}
