package com.pi314.orders.service.impl;

import com.pi314.orders.model.entity.*;
import com.pi314.orders.repository.*;
import com.pi314.orders.service.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfilServiceImpl implements ProfilService {
  private final ProfilRepository profilRepository;

  @Override
  public Profil findByName(String name) {
    return profilRepository.findByName(name);
  }

  @Override
  public Profil findById(Long profilId) {
    return profilRepository.findById(profilId).orElseThrow();
  }

    @Override
    public List<Profil> findAll() {
    return profilRepository.findAll();
    }
}
