package com.pi314.orders.repository;

import com.pi314.orders.model.entity.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);

    List<Order> findByUserAndCreatedAtBetween(User user,LocalDate startDate, LocalDate endDate);

    List<Order> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
}