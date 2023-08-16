package com.pi314.orders.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String door;
    private String stranicа;
    private Long drawerId;
    private Long pilasterId;
    private Long kornizId;
}
