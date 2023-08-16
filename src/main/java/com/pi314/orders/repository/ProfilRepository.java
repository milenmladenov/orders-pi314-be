package com.pi314.orders.repository;

import com.pi314.orders.model.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {
    Profil findByName(String name);
}
