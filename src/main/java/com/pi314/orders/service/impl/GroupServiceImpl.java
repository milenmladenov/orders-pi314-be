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
public class GroupServiceImpl implements GroupService {
  private final DoorService doorService;
  private final ModelService modelService;
  private final FolioService folioService;
  private final HandleService handleService;
  private final ProfilService profilService;
  private final GroupRepository groupRepository;
  private final ModelMapperService modelMapperService;

  @Override
  public GroupDTO createGroup(
      String doorName,
      String modelName,
      String handleName,
      String folioName,
      String profilName,
      Long height,
      Long width,
      Double number,
      String detailType,
      boolean isBothSidesLaminated) {
    Door door = doorService.findByName(doorName);
    Model model = modelService.findByName(modelName);
    Handle handle = handleService.findByName(handleName);
    Folio folio = folioService.findByName(folioName);
    Profil profil = profilService.findByName(profilName);

    return GroupDTO.builder()
        .door(modelMapperService.map(door, DoorDTO.class))
        .model(modelMapperService.map(model, ModelDTO.class))
        .handle(modelMapperService.map(handle, HandleDTO.class))
        .folio(modelMapperService.map(folio, FolioDTO.class))
        .profil(modelMapperService.map(profil, ProfilDTO.class))
        .detailType(detailType)
        .height(height)
        .width(width)
        .number(number)
        .isBothSidesLaminated(isBothSidesLaminated)
        .build();
  }

  @Override
  public Group save(GroupDTO groupDTO, double groupTotalPrice) {
    Door door = doorService.findByName(groupDTO.getDoor().getName());
    Model model = modelService.findByName(groupDTO.getModel().getName());
    Handle handle = handleService.findByName(groupDTO.getHandle().getName());
    Folio folio = folioService.findByName(groupDTO.getFolio().getName());
    Profil profil = profilService.findByName(groupDTO.getProfil().getName());
    Group group =
        Group.builder()
            .modelId(model.getId())
            .doorId(door.getId())
            .profilId(profil.getId())
            .folioId(folio.getId())
            .handleId(handle.getId())
            .detailType(groupDTO.getDetailType())
            .height(groupDTO.getHeight())
            .width(groupDTO.getWidth())
            .number(groupDTO.getNumber())
            .isBothSidesLaminated(groupDTO.isBothSidesLaminated())
            .groupTotalPrice(groupTotalPrice)
            .groupTotalSqrt((((double) groupDTO.getHeight() / 1000) * ((double) groupDTO.getWidth() / 1000)))
            .build();
    return groupRepository.save(group);
  }

  @Override
  public GroupDTO buildGroupDTO(
      Long doorId, Long modelId, Long handleId, Long folioId, Long profilId, Group group) {
    Door door = doorService.findById(doorId);
    Model model = modelService.findById(modelId);
    Handle handle = handleService.findById(handleId);
    Folio folio = folioService.findById(folioId);
    Profil profil = profilService.findById(profilId);
    return GroupDTO.builder()
        .door(modelMapperService.map(door, DoorDTO.class))
        .model(modelMapperService.map(model, ModelDTO.class))
        .handle(modelMapperService.map(handle, HandleDTO.class))
        .folio(modelMapperService.map(folio, FolioDTO.class))
        .profil(modelMapperService.map(profil, ProfilDTO.class))
        .height(group.getHeight())
        .width(group.getWidth())
        .matPrice(group.getMatPrice())
        .groupTotalPrice(group.getGroupTotalPrice())
        .detailType(group.getDetailType())
        .isBothSidesLaminated(group.isBothSidesLaminated())
        .number(group.getNumber())
        .build();
  }

  @Override
  public List<Group> findById(Long id) {
    return groupRepository.findById(id).stream().toList();
  }
}
