package com.pi314.orders.model.entity;

import com.pi314.orders.enums.*;
import jakarta.persistence.*;
import java.time.*;
import java.util.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private Double totalPrice;
  private Double totalSqrt;
  private LocalDate createdAt;
  private LocalDate paidAt;
  private String note;
  private String deliveryAddress;
  private Double discount;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Group> groups;

  @ManyToOne(cascade = CascadeType.ALL)
  private User user;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  private OrderType type;
}
