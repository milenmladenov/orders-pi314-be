package com.pi314.orders.service.impl;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.repository.*;
import com.pi314.orders.service.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FolioServiceImpl implements FolioService {
  private final FolioRepository folioRepository;
  private final ModelMapperService modelMapperService;

  @Override
  public Folio findByName(String name) {
    return folioRepository.findByName(name);
  }

  @Override
  public void bulkImportFolios(List<FolioDTO> folios) {
      folioRepository.saveAll(modelMapperService.mapList(folios,Folio.class));
  }

  @Override
  public Folio findById(Long folioId) {
    return folioRepository.findById(folioId).orElseThrow();
  }
}
