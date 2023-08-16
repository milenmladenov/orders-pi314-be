package com.pi314.orders.model.entity;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private Long doorId;
  private Long modelId;
  private Long profilId;
  private Long handleId;
  private Long folioId;
  private Long orderId;
  private String detailType;
  private double groupTotalSqrt;
  private Double groupTotalPrice;
  private Long height;
  private Long width;
  private Double number;
  private Double matPrice;
  private boolean isBothSidesLaminated;
}
