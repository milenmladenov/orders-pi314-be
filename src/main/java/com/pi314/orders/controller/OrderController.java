package com.pi314.orders.controller;

import com.pi314.orders.enums.*;
import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.service.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
  private final OrderService orderService;
  private final DoorService doorService;
  private final ModelMapperService modelMapperService;

  @PostMapping("/new-order")
  public OrderResponseDTO newOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
    return orderService.createNewOrder(orderRequestDTO);
  }

  @PostMapping("/new-order/preflight")
  public OrderResponseDTO newOrderPreflight(@RequestBody OrderRequestDTO orderRequestDTO) {
    return orderService.preflightNewOrder(orderRequestDTO);
  }



  @GetMapping("/all")
  public List<OrderDTO> returnAllOrders(){
    return orderService.returnAllOrders();
  }

  @GetMapping("/{orderId}")
  public OrderDTO returnAllOrdersForLoggedUser(@PathVariable Long orderId) {
    return orderService.returnOrderById(orderId);
  }

  @GetMapping("/door")
  public Door door(@RequestBody String name) {
    return doorService.findByName(name);
  }

  @GetMapping("/doors")
  public List<Door> doors(){
    return doorService.allDoors();
  }
}
