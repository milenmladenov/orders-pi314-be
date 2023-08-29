package com.pi314.orders.service.impl;

import com.pi314.orders.enums.*;
import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.repository.*;
import com.pi314.orders.service.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;
import lombok.*;
import org.springframework.stereotype.*;

import static com.pi314.orders.enums.UserRole.ADMIN;
import static com.pi314.orders.enums.UserRole.USER;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final ModelMapperService modelMapperService;
  private final UserService userService;
  private final OrderRepository orderRepository;
  private final GroupService groupService;
  List<String> kornizi = List.of("Корниз - К1", "Корниз - К2", "Корниз - К3");
  List<String> pilastri =
      List.of(
          "Пиластър - P1",
          "Пиластър - P2",
          "Пиластър - P3",
          "Пиластър - P4",
          "Пиластър - P5",
          "Пиластър - P6",
          "Пиластър - P7",
          "Пиластър - P8",
          "Пиластър - P9",
          "Пиластър - P10");
  // Calculate the order total price and return it.

  @Override
  public OrderResponseDTO createNewOrder(OrderRequestDTO orderRequestDTO) {
    List<Group> groups = new ArrayList<>();
    List<Double> groupTotalPrices =
        calculatePrices(orderRequestDTO, setOrderType()).getGroupTotalPrices();

    for (int i = 0; i < orderRequestDTO.getGroups().size(); i++) {
      GroupDTO group = orderRequestDTO.getGroups().get(i);
      double groupTotalPrice = groupTotalPrices.get(i); // Get the total price for this group

      Group savedGroup = groupService.save(group, groupTotalPrice);
      groups.add(savedGroup);
    }

    OrderResponseDTO orderResponseDTO =
        new OrderResponseDTO(
            orderRequestDTO.getGroups(),
            calculatePrices(orderRequestDTO, setOrderType()).getOrderTotalPrice());
    Order order =
        Order.builder()
            .user(getLoggedUser())
            .type(setOrderType())
            .status(OrderStatus.CREATED)
            .totalPrice(orderResponseDTO.totalPrice())
            .createdAt(LocalDate.now())
            .groups(groups)
            .build();
    orderRepository.save(order);

    return orderResponseDTO;
  }

  @Override
  public OrderResponseDTO preflightNewOrder(OrderRequestDTO orderRequestDTO) {
    return new OrderResponseDTO(
        orderRequestDTO.getGroups(),
        calculatePrices(orderRequestDTO, setOrderType()).getOrderTotalPrice());
  }

  @Override
  public OrderResponseDTO createNewGroup(OrderRequestDTO orderRequestDTO) {
    return null;
  }

  @Override
  public List<OrderDTO> returnAllOrders() {
    List<Order> allOrdersFromDB = new ArrayList<>();
    if (Objects.equals(getLoggedUser().getRole(), ADMIN.toString())) {
      allOrdersFromDB = orderRepository.findAll();
    }
    if (Objects.equals(getLoggedUser().getRole(), USER.toString())) {
      allOrdersFromDB = orderRepository.findByUser(getLoggedUser());
    }
    List<OrderDTO> allOrders = modelMapperService.mapList(allOrdersFromDB, OrderDTO.class);
    mapOrderGroups(allOrdersFromDB, allOrders);
    System.out.println(getLoggedUser().getRole());
    return allOrders.stream()
        .sorted(Comparator.comparing(OrderDTO::getId).reversed())
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderDTO> returnAllOrdersPerUser() {
    List<Order> allOrdersFromDB = orderRepository.findByUser(getLoggedUser());
    List<OrderDTO> allOrders = modelMapperService.mapList(allOrdersFromDB, OrderDTO.class);
    mapOrderGroups(allOrdersFromDB, allOrders);
    return allOrders.stream()
        .sorted(Comparator.comparing(OrderDTO::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

    @Override
    public void changeOrderStatus(Long orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId).orElseThrow();
    order.setStatus(status);
    orderRepository.save(order);
    }

    @Override
  public OrderDTO returnOrderById(Long orderId) {
    List<Order> order = orderRepository.findById(orderId).stream().toList();
    List<OrderDTO> allOrders = modelMapperService.mapList(order, OrderDTO.class);
    mapOrderGroups(order, allOrders);
    return allOrders.get(0);
  }

  private void mapOrderGroups(List<Order> allOrdersFromDB, List<OrderDTO> allOrders) {

    for (Order orderEntity : allOrdersFromDB) {
      List<GroupDTO> responseGroups = new ArrayList<>();
      for (Group orderGroup : orderEntity.getGroups()) {
        List<Group> groups = groupService.findById(orderGroup.getId());

        for (Group group : groups) {
          responseGroups.add(
              groupService.buildGroupDTO(
                  group.getDoorId(),
                  group.getModelId(),
                  group.getHandleId(),
                  group.getFolioId(),
                  group.getProfilId(),
                  group));
          Optional<OrderDTO> orderDTO =
              allOrders.stream()
                  .filter(order -> order.getId().equals(orderEntity.getId()))
                  .findFirst();
          orderDTO.ifPresent(singleOrderDTO -> singleOrderDTO.setGroups(responseGroups));
        }
      }
    }
  }

  private User getLoggedUser() {
    return userService.getLoggedUser().get();
  }

  private Prices calculatePrices(OrderRequestDTO orderRequestDTO, OrderType orderType) {
    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    Prices prices = new Prices();
    List<Double> groupTotalPrices = new ArrayList<>();
    double orderTotalPrice = 0;

    List<GroupDTO> groupList = new ArrayList<>();
    for (GroupDTO group : orderRequestDTO.getGroups()) {
      double groupTotalPrice = 0;
      GroupDTO groupDTO =
          groupService.createGroup(
              group.getDoor().getName(),
              group.getModel().getName(),
              group.getHandle().getName(),
              group.getFolio().getName(),
              group.getProfil().getName(),
              group.getHeight(),
              group.getWidth(),
              group.getNumber(),
              group.getDetailType(),
              group.isBothSidesLaminated());

      double height = group.getHeight() / 1000.0;
      double width = group.getWidth() / 1000.0;
      double squareMeters = height * width;

      double doorPrice = groupDTO.getDoor() != null ? groupDTO.getDoor().getPrice() : 0;
      double modelPrice = groupDTO.getModel() != null ? groupDTO.getModel().getPrice() : 0;
      double folioPrice = groupDTO.getFolio() != null ? groupDTO.getFolio().getPrice() : 0;
      double handlePrice = groupDTO.getHandle() != null ? groupDTO.getHandle().getPrice() : 0;
      double profilPrice = groupDTO.getProfil() != null ? groupDTO.getProfil().getPrice() : 0;

      if (!kornizi.contains(groupDTO.getDetailType())
          && !pilastri.contains(groupDTO.getDetailType())) {
        groupTotalPrice +=
            ((squareMeters * (doorPrice + modelPrice + folioPrice)) + handlePrice + profilPrice)
                * groupDTO.getNumber();
      }

      if (kornizi.contains(groupDTO.getDetailType())) {
        groupTotalPrice += groupDTO.getNumber() * 76;
      }
      if (pilastri.contains(groupDTO.getDetailType())) {
        groupTotalPrice += height * 12;
      }
      groupList.add(groupDTO);
      group.setGroupTotalPrice(Double.parseDouble(decimalFormat.format(groupTotalPrice * 1.2)));
      groupTotalPrices.add(group.getGroupTotalPrice());
      orderTotalPrice += groupTotalPrice;
    }
    orderRequestDTO.setGroups(groupList);

    if (orderType == OrderType.BY_HAND || orderType == OrderType.IMPORTED) {
      orderTotalPrice *= 1.3;
    }

    double totalPriceWithVAT = orderTotalPrice * 1.2;
    prices.setOrderTotalPrice(Double.parseDouble(decimalFormat.format(totalPriceWithVAT)));
    prices.setGroupTotalPrices(groupTotalPrices);
    return prices;
  }

  //  private Double calculateGroupTotalPrice(OrderRequestDTO orderRequestDTO) {
  //    DecimalFormat decimalFormat = new DecimalFormat("#.00");
  //    double groupTotalPrice = 0;
  //    List<GroupDTO> groupList = new ArrayList<>();
  //    for (GroupDTO group : orderRequestDTO.getGroups()) {
  //      GroupDTO groupDTO =
  //          groupService.createGroup(
  //              group.getDoor().getName(),
  //              group.getModel().getName(),
  //              group.getHandle().getName(),
  //              group.getFolio().getName(),
  //              group.getProfil().getName(),
  //              group.getHeight(),
  //              group.getWidth(),
  //              group.getNumber(),
  //              group.getDetailType(),
  //              group.isBothSidesLaminated());
  //
  //      double height = groupDTO.getHeight() / 1000.0;
  //      double width = groupDTO.getWidth() / 1000.0;
  //      double squareMeters = height * width;
  //
  //      double doorPrice = groupDTO.getDoor() != null ? groupDTO.getDoor().getPrice() : 0;
  //      double modelPrice = groupDTO.getModel() != null ? groupDTO.getModel().getPrice() : 0;
  //      double folioPrice = groupDTO.getFolio() != null ? groupDTO.getFolio().getPrice() : 0;
  //      double handlePrice = groupDTO.getHandle() != null ? groupDTO.getHandle().getPrice() : 0;
  //      double profilPrice = groupDTO.getProfil() != null ? groupDTO.getProfil().getPrice() : 0;
  //
  //      if (!kornizi.contains(groupDTO.getDetailType())
  //          && !pilastri.contains(groupDTO.getDetailType())) {
  //        groupTotalPrice +=
  //            ((squareMeters * (doorPrice + modelPrice + folioPrice)) + handlePrice + profilPrice)
  //                * groupDTO.getNumber();
  //      }
  //
  //      if (kornizi.contains(groupDTO.getDetailType())) {
  //        groupTotalPrice += groupDTO.getNumber() * 76;
  //      }
  //      if (pilastri.contains(groupDTO.getDetailType())) {
  //        groupTotalPrice += height * 12;
  //      }
  //      groupList.add(groupDTO);
  //      group.setGroupTotalPrice(Double.parseDouble(decimalFormat.format(groupTotalPrice * 1.2)));
  //    }
  //    orderRequestDTO.setGroups(groupList);
  //    return Double.parseDouble(decimalFormat.format(groupTotalPrice));
  //  }

  private OrderType setOrderType() {
    if (getLoggedUser().getRole().equals("ADMIN")) {
      return OrderType.BY_HAND;
    }
    return OrderType.BY_USER;
  }
}
