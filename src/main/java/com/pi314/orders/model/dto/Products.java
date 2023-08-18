package com.pi314.orders.model.dto;

import java.util.*;
import lombok.*;

public record Products(
    List<DoorDTO> doors,
    List<ModelDTO> models,
    List<HandleDTO> handles,
    List<FolioDTO> folios,
    List<ProfilDTO> profils) {}
