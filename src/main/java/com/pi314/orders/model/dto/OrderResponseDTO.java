package com.pi314.orders.model.dto;

import java.util.*;

public record OrderResponseDTO(List<GroupDTO> groups, Double totalPrice) {}
