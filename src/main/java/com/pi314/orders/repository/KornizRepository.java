package com.pi314.orders.repository;

import com.pi314.orders.model.entity.*;
import org.springframework.data.jpa.repository.*;

public interface KornizRepository extends JpaRepository<Korniz,Long> {
  Korniz  findByName(String name);

}
