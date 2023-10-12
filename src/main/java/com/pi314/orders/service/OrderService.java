package com.pi314.orders.service;

import com.pi314.orders.enums.*;
import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.*;

public interface OrderService {

  OrderResponseDTO createNewOrder(OrderRequestDTO orderRequestDTO) throws MessagingException, UnsupportedEncodingException;

  OrderResponseDTO preflightNewOrder(OrderRequestDTO orderRequestDTO);

  OrderResponseDTO createNewGroup(OrderRequestDTO orderRequestDTO);

  List<OrderDTO> returnAllOrders();

  List<OrderDTO> returnAllOrdersPerUser();

  void changeOrderStatus(Long orderId, OrderStatus status);

  OrderDTO returnOrderById(Long orderId);

  void editOrder(OrderRequestDTO orderRequestDTO, Long orderId);
}
