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
  private final TypeService typeService;
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
      Long length,
      Double number,
      TypeDTO detailType,
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
        .length(length)
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
    Type detailType = typeService.save(groupDTO);
    Group group =
        Group.builder()
            .modelId(model.getId())
            .doorId(door.getId())
            .profilId(profil.getId())
            .folioId(folio.getId())
            .handleId(handle.getId())
            .height(groupDTO.getHeight())
            .width(groupDTO.getWidth())
            .length(groupDTO.getLength())
            .number(groupDTO.getNumber())
            .deliveryAddress(groupDTO.getDeliveryAddress())
            .note(groupDTO.getNote())
            .discount(groupDTO.getDiscount())
            .isBothSidesLaminated(groupDTO.isBothSidesLaminated())
            .groupTotalPrice(groupTotalPrice)
            .groupTotalSqrt(
                (((double) groupDTO.getHeight() / 1000) * ((double) groupDTO.getWidth() / 1000)))
            .build();
    group.setDetailType(detailType);
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
        .length(group.getLength())
        .deliveryAddress(group.getDeliveryAddress())
        .discount(group.getDiscount())
        .note(group.getNote())
        .matPrice(group.getMatPrice())
        .groupTotalPrice(group.getGroupTotalPrice())
        .detailType(modelMapperService.map(group.getDetailType(), TypeDTO.class))
        .isBothSidesLaminated(group.isBothSidesLaminated())
        .number(group.getNumber())
        .build();
  }

  @Override
  public Group buildGroup(GroupDTO groupDTO, double groupTotalPrice) {
    Door door = doorService.findByName(groupDTO.getDoor().getName());
    Model model = modelService.findByName(groupDTO.getModel().getName());
    Handle handle = handleService.findByName(groupDTO.getHandle().getName());
    Folio folio = folioService.findByName(groupDTO.getFolio().getName());
    Profil profil = profilService.findByName(groupDTO.getProfil().getName());
    Type detailType = typeService.save(groupDTO);
    return Group.builder()
        .modelId(model.getId())
        .doorId(door.getId())
        .profilId(profil.getId())
        .folioId(folio.getId())
        .handleId(handle.getId())
        .height(groupDTO.getHeight())
        .width(groupDTO.getWidth())
        .length(groupDTO.getLength())
        .number(groupDTO.getNumber())
        .deliveryAddress(groupDTO.getDeliveryAddress())
        .note(groupDTO.getNote())
        .discount(groupDTO.getDiscount())
        .isBothSidesLaminated(groupDTO.isBothSidesLaminated())
        .groupTotalPrice(groupTotalPrice)
        .detailType(detailType)
        .groupTotalSqrt(
            (((double) groupDTO.getHeight() / 1000) * ((double) groupDTO.getWidth() / 1000)))
        .build();
  }

  @Override
  public List<Group> findById(Long id) {
    return groupRepository.findById(id).stream().toList();
  }

  @Override
  public Products returnAllProducts() {
    List<DoorDTO> doors = modelMapperService.mapList(doorService.allDoors(), DoorDTO.class);
    List<ModelDTO> models = modelMapperService.mapList(modelService.findAll(), ModelDTO.class);
    List<HandleDTO> handles = modelMapperService.mapList(handleService.findAll(), HandleDTO.class);
    List<FolioDTO> folios = modelMapperService.mapList(folioService.findAll(), FolioDTO.class);
    List<ProfilDTO> profils = modelMapperService.mapList(profilService.findAll(), ProfilDTO.class);
    return new Products(doors, models, handles, folios, profils);
  }

  @Override
  public Double getHandlePrice(String handleName) {
    Handle handle = handleService.findByName(handleName);
    return handle.getPrice();
  }
}
