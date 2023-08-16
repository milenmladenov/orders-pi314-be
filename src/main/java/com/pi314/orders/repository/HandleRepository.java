package com.pi314.orders.repository;

import com.pi314.orders.model.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface HandleRepository extends JpaRepository<Handle,Long> {
    Handle findByName (String name);
}
