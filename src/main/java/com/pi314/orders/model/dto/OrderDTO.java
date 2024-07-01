package com.pi314.orders.model.dto;

import com.pi314.orders.enums.*;
import lombok.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Data
public class OrderDTO {
    private Long id;
    private List<GroupDTO> groups;
    private UserDTO user;
    private OrderStatus status;
    private OrderType type;
    private Double discount;
    private LocalDate createdAt;
    private Double totalPrice;
    private String note;
    private String deliveryAddress;
    private Double handlePrice;
    private String orderUuid;


    public LocalDate getLocalDate() {
        return LocalDate.parse(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

}
