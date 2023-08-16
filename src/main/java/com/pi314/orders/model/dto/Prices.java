package com.pi314.orders.model.dto;

import lombok.*;

import java.util.*;

@Data
public class Prices {
  private List<Double> groupTotalPrices;
  private double orderTotalPrice;
}
