package com.pi314.orders.repository;

import com.pi314.orders.model.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

    List<Group> findByOrderId(Long id);
}
