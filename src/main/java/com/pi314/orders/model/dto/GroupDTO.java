package com.pi314.orders.model.dto;

import lombok.*;
import org.springframework.lang.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
  private DoorDTO door;
  private ModelDTO model;
  private HandleDTO handle;
  private FolioDTO folio;
  private ProfilDTO profil;
  private Long height;
  private Long width;
  private Double number;
  private Double matPrice;
  private boolean isBothSidesLaminated;
  private double groupTotalPrice;
  private String detailType;
}
