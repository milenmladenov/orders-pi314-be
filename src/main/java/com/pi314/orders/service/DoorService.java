package com.pi314.orders.service;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import java.util.*;

public interface DoorService {


  Door findByName(String name);

  List<Door> allDoors();

  void bulkImportDoors(List<DoorDTO> doors);

  Door findById(Long doorId);
}
