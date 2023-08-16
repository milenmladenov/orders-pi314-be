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
public class ModelServiceImpl implements ModelService {
  private final ModelRepository modelRepository;
  private final ModelMapperService modelMapperService;

  @Override
  public Model findByName(String name) {
    return modelRepository.findByName(name);
  }

  @Override
  public void bulkImportModels(List<ModelDTO> models) {
    modelRepository.saveAll(modelMapperService.mapList(models, Model.class));
  }

  @Override
  public Model findById(Long modelId) {
    return modelRepository.findById(modelId).orElseThrow();
  }
}
