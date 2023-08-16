package com.pi314.orders.service.impl;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.repository.*;
import com.pi314.orders.service.*;
import java.util.*;
import lombok.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class DoorServiceImpl implements DoorService {
  private final DoorRepository doorRepository;
  private final ModelMapperService modelMapperService;

  @Override
  public Door findByName(String name) {
    return doorRepository.findByName(name);
  }

  @Override
  public List<Door> allDoors() {
    return doorRepository.findAll();
  }

  @Override
  public void bulkImportDoors(List<DoorDTO> doors) {
    doorRepository.saveAll(modelMapperService.mapList(doors, Door.class));
  }

  @Override
  public Door findById(Long doorId) {
    return doorRepository.findById(doorId).orElseThrow();
  }
}
