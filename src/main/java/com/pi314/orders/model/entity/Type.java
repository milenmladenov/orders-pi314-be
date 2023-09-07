package com.pi314.orders.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String material;
    private String type;
}
