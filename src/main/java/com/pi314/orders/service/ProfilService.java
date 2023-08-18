package com.pi314.orders.service;

import com.pi314.orders.model.entity.*;

import java.util.*;

public interface ProfilService {
    Profil findByName(String name);

    Profil findById(Long profilId);

    List<Profil> findAll();
}
