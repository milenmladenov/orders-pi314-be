package com.pi314.orders.model.dto;


import java.util.*;
import lombok.*;

@Data
public class OrderRequestDTO {
  private List<GroupDTO> groups;
  private String username;
}
