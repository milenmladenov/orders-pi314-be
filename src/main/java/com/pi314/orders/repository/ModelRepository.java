package com.pi314.orders.repository;

import com.pi314.orders.model.entity.*;
import org.springframework.data.jpa.repository.*;

public interface ModelRepository  extends JpaRepository<Model,Long> {
    Model findByName (String name);
}
