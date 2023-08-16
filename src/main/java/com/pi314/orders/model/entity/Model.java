package com.pi314.orders.model.entity;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Model {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String name;

  private Double price;
}
