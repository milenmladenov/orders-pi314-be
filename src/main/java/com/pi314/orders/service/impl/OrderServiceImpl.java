package com.pi314.orders.service.impl;

import static com.pi314.orders.enums.UserRole.ADMIN;
import static com.pi314.orders.enums.UserRole.USER;

import com.pi314.orders.enums.*;
import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.repository.*;
import com.pi314.orders.service.*;

import java.io.UnsupportedEncodingException;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

import jakarta.mail.MessagingException;
import lombok.*;
import org.springframework.beans.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ModelMapperService modelMapperService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final GroupService groupService;
    private final EmailSenderService emailSenderService;

    @Override
    public OrderResponseDTO createNewOrder(OrderRequestDTO orderRequestDTO) throws MessagingException, UnsupportedEncodingException {
        List<Group> groups = new ArrayList<>();
        List<Double> groupTotalPrices =
                calculatePrices(orderRequestDTO, setOrderType()).getGroupTotalPrices();
        Double orderRequestDTODiscount = orderRequestDTO.getDiscount();
        Double userDiscount = Double.valueOf(getLoggedUser().getAppliedDiscount());

        for (int i = 0; i < orderRequestDTO.getGroups().size(); i++) {
            GroupDTO group = orderRequestDTO.getGroups().get(i);
            double groupTotalPrice = groupTotalPrices.get(i); // Get the total price for this group

            Group savedGroup = groupService.save(group, groupTotalPrice);
            groups.add(savedGroup);
        }

        Double handlePrice =
                groupService.getHandlePrice(orderRequestDTO.getGroups().get(0).getHandle().getName());

        Order order =
                Order.builder()
                        .user(getLoggedUser())
                        .type(setOrderType())
                        .status(OrderStatus.CREATED)
                        .totalPrice(calculatePrices(orderRequestDTO, setOrderType()).getOrderTotalPrice())
                        .createdAt(LocalDate.now())
                        .groups(groups)
                        .note(orderRequestDTO.getNote())
                        .deliveryAddress(orderRequestDTO.getDeliveryAddress())
                        .build();
        order.setDiscount(orderRequestDTODiscount);
            if (orderRequestDTODiscount == 0 && userDiscount != 0) {
                order.setDiscount(userDiscount);
            }
        orderRepository.save(order);
            order.setOrderUuid(order.getId().toString() + order.getCreatedAt().toString());
            orderRepository.save(order);
        emailSenderService.sendCreatedOrderEmail(order);
        return new OrderResponseDTO(
                orderRequestDTO.getGroups(),
                calculatePrices(orderRequestDTO, setOrderType()).getOrderTotalPrice(),
                order.getDiscount(),
                handlePrice,
                getLoggedUser().getOrderAddress(),
                order.getId());
    }

    @Override
    public OrderResponseDTO preflightNewOrder(OrderRequestDTO orderRequestDTO) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        Double handlePrice =
                Double.parseDouble(decimalFormat.format(groupService.getHandlePrice("дръжка H1")));

        return new OrderResponseDTO(
                orderRequestDTO.getGroups(),
                calculatePrices(orderRequestDTO, setOrderType()).getOrderTotalPrice(),
                0.0,
                handlePrice,
                getLoggedUser().getOrderAddress(),
                0L);
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
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        Double handlePrice =
                Double.parseDouble(decimalFormat.format(groupService.getHandlePrice("дръжка H1")));
        List<Order> order = orderRepository.findById(orderId).stream().toList();
        List<OrderDTO> allOrders = modelMapperService.mapList(order, OrderDTO.class);
        mapOrderGroups(order, allOrders);
        allOrders.get(0).setHandlePrice(handlePrice);
        return allOrders.get(0);
    }

    @Override
    public void editOrder(OrderRequestDTO orderRequestDTO, Long orderId) {
        Order orderEntity = orderRepository.findById(orderId).orElseThrow();
        List<Group> groups = new ArrayList<>();
        List<Double> groupTotalPrices =
                calculatePrices(orderRequestDTO, setOrderType()).getGroupTotalPrices();

        for (int i = 0; i < orderRequestDTO.getGroups().size(); i++) {
            GroupDTO group = orderRequestDTO.getGroups().get(i);
            double groupTotalPrice = groupTotalPrices.get(i); // Get the total price for this group

            Group savedGroup = groupService.buildGroup(group, groupTotalPrice);
            groups.add(savedGroup);
        }
        orderEntity.setGroups(groups);
        orderEntity.setTotalPrice(
                calculatePrices(orderRequestDTO, setOrderType()).getOrderTotalPrice());
        orderRepository.save(orderEntity);
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
        double totalSquareMeters = 0;

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
                            group.getLength(),
                            group.getNumber(),
                            group.getDetailType(),
                            group.isBothSidesLaminated());

            double height = group.getHeight() / 1000.0;
            double width = group.getWidth() / 1000.0;
            double squareMeters = (height * width) * groupDTO.getNumber();
            totalSquareMeters += squareMeters;
            System.out.println(totalSquareMeters);
            double doorPrice = groupDTO.getDoor() != null ? groupDTO.getDoor().getPrice() : 0;
            double modelPrice = groupDTO.getModel() != null ? groupDTO.getModel().getPrice() : 0;
            double folioPrice = groupDTO.getFolio() != null ? groupDTO.getFolio().getPrice() : 0;
            double handlePrice = groupDTO.getHandle() != null ? groupDTO.getHandle().getPrice() : 0;
            double profilPrice = groupDTO.getProfil() != null ? groupDTO.getProfil().getPrice() : 0;

            if (!groupDTO.getDetailType().getMaterial().equals("Корниз")
                    && !(groupDTO.getDetailType().getMaterial().equals("Пиластър"))) {
                groupTotalPrice +=
                        ((squareMeters * (doorPrice + modelPrice + folioPrice)) + handlePrice + profilPrice);
            }

            if (groupDTO.getDetailType().getMaterial().equals("Корниз")) {
                if (groupDTO.getLength() == 2360) {
                    groupTotalPrice += groupDTO.getNumber() * 76;
                }
                if (group.getLength() == 1160) {
                    groupTotalPrice += groupDTO.getNumber() * 38;
                }
            }

            if (groupDTO.getDetailType().getMaterial().equals("Пиластър")) {
                groupTotalPrice += height * 12;
            }
            groupList.add(groupDTO);

            if (group.isBothSidesLaminated()) {
                groupTotalPrice *= 1.9;
            }
            group.setGroupTotalPrice(Double.parseDouble(decimalFormat.format(groupTotalPrice * 1.2)));
            groupTotalPrices.add(group.getGroupTotalPrice());
            orderTotalPrice += groupTotalPrice;
        }
        orderRequestDTO.setGroups(groupList);

        if (totalSquareMeters <= 1.5) {
            orderTotalPrice *= 1.3;
        }
        if (orderRequestDTO.getDiscount() != null && (orderRequestDTO.getDiscount() > 0)) {
            double discount = orderTotalPrice * (orderRequestDTO.getDiscount() * 0.01);
            orderTotalPrice -= discount;
        }
        if (orderType == OrderType.BY_USER && getLoggedUser().getAppliedDiscount() == null) {
            double discount = orderTotalPrice * 0.05;
            orderTotalPrice -= discount;
        }
        if (getLoggedUser().getAppliedDiscount() != null) {

            double customerAppliedDiscount =
                    orderTotalPrice * (getLoggedUser().getAppliedDiscount() * 0.01);
            orderTotalPrice -= customerAppliedDiscount;
        }

        double totalPriceWithVAT = orderTotalPrice * 1.2;
        System.out.println(
                "Price after vat" + Double.parseDouble(decimalFormat.format(totalPriceWithVAT)));

        prices.setOrderTotalPrice(Double.parseDouble(decimalFormat.format(totalPriceWithVAT)));
        prices.setGroupTotalPrices(groupTotalPrices);
        return prices;
    }

    private OrderType setOrderType() {
        if (getLoggedUser().getRole().equals("ADMIN")) {
            return OrderType.BY_HAND;
        }
        return OrderType.BY_USER;
    }
}
