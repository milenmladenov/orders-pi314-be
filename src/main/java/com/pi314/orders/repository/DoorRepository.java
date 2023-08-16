package com.pi314.orders.repository;

import com.pi314.orders.model.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface DoorRepository extends JpaRepository<Door, Long> {
  Door findByName(String name);
}
