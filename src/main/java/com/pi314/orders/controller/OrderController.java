package com.pi314.orders.controller;

import com.pi314.orders.enums.*;
import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.service.*;
import jakarta.mail.MessagingException;
import lombok.*;

import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;
    private final DoorService doorService;
    private final ModelMapperService modelMapperService;

    @PostMapping("/new-order")
    public OrderResponseDTO newOrder(@RequestBody OrderRequestDTO orderRequestDTO)
            throws MessagingException, UnsupportedEncodingException {
        return orderService.createNewOrder(orderRequestDTO);
    }

    @PostMapping("/new-order/preflight")
    public OrderResponseDTO newOrderPreflight(@RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.preflightNewOrder(orderRequestDTO);
    }

    @PostMapping("/{orderId}/change-status/{orderStatus}")
    public void changeOrderStatus(@PathVariable Long orderId, @PathVariable OrderStatus orderStatus)
            throws MessagingException, UnsupportedEncodingException {
        orderService.changeOrderStatus(orderId, orderStatus);
    }


    @GetMapping("/all")
    public List<OrderDTO> returnAllOrders(@RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return orderService.returnAllOrders(startDate, endDate);
    }

    @GetMapping("/{orderId}")
    public OrderDTO returnAllOrdersForLoggedUser(@PathVariable Long orderId) {
        return orderService.returnOrderById(orderId);
    }

    @PutMapping("/edit-order/{orderId}")
    public void editOrder(@RequestBody OrderRequestDTO orderRequestDTO, @PathVariable Long orderId) {
        orderService.editOrder(orderRequestDTO, orderId);
    }


    @GetMapping("/door")
    public Door door(@RequestBody String name) {
        return doorService.findByName(name);
    }

    @GetMapping("/doors")
    public List<Door> doors() {
        return doorService.allDoors();
    }
}
