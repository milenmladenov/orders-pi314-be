package com.pi314.orders.controller;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import com.pi314.orders.service.*;
import java.util.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/import")
public class ImportController {
  private final DoorService doorService;
  private final ModelService modelService;
  private final FolioService folioService;

  @PostMapping("/doors")
  public void bulkImportDoors(@RequestBody List<DoorDTO> doors) {
    doorService.bulkImportDoors(doors);
  }

  @PostMapping("/models")
  public String bulkImportModels(@RequestBody List<ModelDTO> models) {
    modelService.bulkImportModels(models);
    return "imported";
  }

  @PostMapping("/folios")
  public String bulkImportFolios(@RequestBody List<FolioDTO> folios) {
    folioService.bulkImportFolios(folios);
    return "imported";
  }
}
