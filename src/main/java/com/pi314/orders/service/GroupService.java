package com.pi314.orders.service;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;
import java.util.*;

public interface GroupService {

  GroupDTO createGroup(
      String doorName,
      String modelName,
      String handleName,
      String folioName,
      String profilName,
      Long height,
      Long width,
      Double number,
      String detailType,
      boolean isBothSidesLaminated);

  Group save(GroupDTO groupDTO, double groupTotalPrice);

  GroupDTO buildGroupDTO(
      Long doorId, Long modelId, Long handleId, Long folioId, Long profilId, Group group);

  List<Group> findById(Long id);

  Products returnAllProducts();

  Double getHandlePrice(String handleName);
}
